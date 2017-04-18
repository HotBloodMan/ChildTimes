package com.lee.privatecustom.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.Semaphore;

public class ImageLoader {
	//线程池中，线程的数量。为了保证最好的并发性
	//这个数量与设备CPU的核数一样
	private static int threadCount;
	//线程池
	private static ExecutorService exec;
	//上下文对象
	private static Context context;
	//当线程池中的工作线程获得图像后
	//需要将图像通过uiHandler提交到主线程
	//进而在ImageView中进行显示
	private static Handler uiHandler;
	//当生产者向任务队列中添加了需要执行的任务后
	//生产者会向该pollHandler发送一个Message
	//通知它去任务队列中取任务放到线程池中执行
	private static Handler pollHandler;
	//与pollHandler相依相偎的一个工作线程
	//pollHandler把收到的Message都提交到该线程
	//该线程的Looper从MessageQueue中把消息取出
	//再返回给pollHandler执行
	private static Thread pollThread;
	//任务队列
	//生产者将任务放到该队列中
	//消费中从该队列中取任务执行
	private static LinkedBlockingDeque<Runnable> tasks;
	//为下载的图片提供内存缓存
	//其中键为图片的url地址转的MD5字符串，值为图片本身
	private static LruCache<String, Bitmap> memCache;
	//如果所有的相关属性都未做初始化，则isFirst为true
	//一旦做了初始化，isFrist的值就为false
	private static boolean isFirst = true;

	//用来控制线程池可以取任务的数量
	private static Semaphore pollLock;
	//当主线程加载图像时，向pollHandler发送消息
	//要保证发消息时，pollHandler必须被创建出来了
	private static Semaphore pollHandlerLock = new Semaphore(0);
	//磁盘缓存对象
	private static DiskLruCache diskCache;

	/**
	 * ImageLoader的初始化方法
	 * 把上述所有属性都要进行赋值
	 * @param c
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
	public static void init(Context c){
		if(!isFirst){
			return;
		}
		isFirst = false;
		context = c;
		tasks = new LinkedBlockingDeque<Runnable>();
		threadCount = getCoreNumbers();
		//创建与线程池中线程数量一样多的“许可”
		pollLock = new Semaphore(threadCount);
		//创建线程池
		exec = Executors.newFixedThreadPool(threadCount);
		pollThread = new Thread(){
			@Override
			public void run() {
				Looper.prepare();
				pollHandler = new Handler(){
					@Override
					public void handleMessage(Message msg) {
						//一旦该pollHandler收到消息
						//就意味着任务队列中有了任务
						//就去取任务，放到线程池中执行
						try {
							Runnable task = tasks.getLast();
							exec.execute(task);
							pollLock.acquire();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				};
				//释放许可
				pollHandlerLock.release();
				Looper.loop();

			}
		};
		pollThread.start();
		//通过Handler(looper)这样的构造方式
		//保证uiHandler是与主线程相依相偎
		uiHandler = new Handler(Looper.getMainLooper()){
			@Override
			public void handleMessage(Message msg) {
				//TODO
				//用来将获得的图片放到ImageView中显示
				//需要解决一个“反复”显示的问题

				switch (msg.what) {
					case 101:
						TaskBean bean = (TaskBean) msg.obj;
						ImageView iv = bean.iv;
						Bitmap bimtap = bean.bitmap;
						String tag = bean.tag;
						if(iv.getTag().toString().equals(tag)){
							iv.setImageBitmap(bimtap);
						}
						break;

					default:
						super.handleMessage(msg);
						break;
				}
			}
		};
		//初始化内存缓存
		memCache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory()/4)){
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getHeight()*value.getRowBytes();
			}
		};
		//初始化磁盘缓存
		try {
			diskCache = DiskLruCache.open(getCahceDir(), 1, 1, 1024*1024*8);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	private static File getCahceDir() {
		//要获取系统提供的可以进行缓存功能的路径
		String dir = context.getCacheDir().getPath();
		String name = "imageloadercache";
		return new File(dir,name);
	}
	/*
	 * 加载制定位置的图形到ImageView中显示
	 */
	public static void loadImage(final ImageView iv,final String url){
		Bitmap result = null;
		final String tag = getMD5(url);
		result = memCache.get(tag);

		iv.setTag(tag);

		if(result!=null){
			Log.d("TAG","图片从内存缓存中加载");
			iv.setImageBitmap(result);
			return;
		}

		try {
			DiskLruCache.Snapshot snap = diskCache.get(tag);
			if(snap!=null){//说明该文件在磁盘缓存中有存储
				Log.d("TAG","图片从磁盘缓存中加载");
				InputStream in = snap.getInputStream(0);
				result = BitmapFactory.decodeStream(in);
				//把图片存储内存缓存
				memCache.put(tag, result);
				iv.setImageBitmap(result);
				in.close();
				return;
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		//如果缓存中没有，添加到任务队列中，去做下载
		tasks.add(new Runnable() {

			@Override
			public void run() {
				//去url指定位置下载图片
				try{
					URL u = new URL(url);
					HttpURLConnection connection = (HttpURLConnection) u.openConnection();
					connection.setDoInput(true);
					connection.setRequestMethod("GET");
					connection .connect();
					InputStream in = connection.getInputStream();
					//bitmap是经过压缩的图片
					Bitmap bitmap = compress(iv,in);
					in.close();
					//将下载的图片放到缓存中缓存
					memCache.put(tag, bitmap);
					//将下载的图片放到磁盘缓存中进行缓存
					DiskLruCache.Editor editor = diskCache.edit(tag);
					OutputStream out = editor.newOutputStream(0);
					bitmap.compress(CompressFormat.JPEG, 100, out);
					editor.commit();
					//写DiskKLruCache的日志文件
					diskCache.flush();
					//????可以吗?
					//用一个bean,两个属性，一个属性引用Bitmap，另一个属性引用要显示该Bitmap的ImageView
					TaskBean bean = new TaskBean();
					bean.bitmap = bitmap;
					bean.iv = iv;
					bean.tag = tag;
					Message.obtain(uiHandler, 101, bean).sendToTarget();
					//释放一个许可，允许线程池继续去取任务
					pollLock.release();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
		//添加了任务后，马上通知pollHandler去任务队列中取任务！
		if(pollHandler==null){
			//等待
			//获取一个“许可”
			try {
				pollHandlerLock.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		Message.obtain(pollHandler).sendToTarget();
	}
	/**
	 * 根据ImageView的大小，对图像进行适当的压缩处理
	 * @param iv
	 * @param in
	 * @return
	 */
	protected static Bitmap compress(ImageView iv, InputStream in) {

		try {
			//先尝试获得ImageView的大小
			int width = iv.getWidth(); //有可能得到正确结果，有可能得到0
			int height = iv.getHeight();//同上

			if(width==0||height==0){
				//怎么办？
				//折中方式1)用固定尺寸100dp?150dp?
				//       2)用设备屏幕的宽/高
				//第一种方式，是使用TypedValue类，用法参考友录项目的CircleImageView写法
				//第二种方式
				//拿到当前设备屏幕的宽度
				width = context.getResources().getDisplayMetrics().widthPixels;
				//拿到当前设备屏幕的高度
				height = context.getResources().getDisplayMetrics().heightPixels;
			}

			//获得图像实际的宽/高
			//首先将InputStream转为byte[]数组
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int len = -1;
			while((len=in.read())!=-1){
				out.write(len);
			}
			byte[] bytes = out.toByteArray();
			out.close();

			Options opts = new Options();
			//让BitmapFactory仅仅取获得byte[]数组所
			//代表的Bitmap文件的尺寸值
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts );
			//利用opts获得byte[]数组表示的图形的宽和高
			int bitmapWidth = opts.outWidth;
			int bitmapHeight = opts.outHeight;
			//压缩。压缩的比例就取决于图片的宽高与前面计算的width和height的比值
			int sampleSize = 1;
			//如果图形的宽或图形高大于我希望的宽或者高
			//就需要进行压缩
			//压缩比就是bitmapWidth*1.0/width或bitmapHeight*1.0/height
			//中更大的哪个值！
			//比如希望尺寸是100*100，实际图形尺寸是3000*4800,压缩比取48
			if(bitmapWidth*1.0/width>1||bitmapHeight*1.0/height>1){
				sampleSize = (int) Math.ceil(Math.max(bitmapWidth*1.0/width, bitmapHeight*1.0/height));
			}
			opts.inSampleSize = sampleSize;
			opts.inJustDecodeBounds = false;
			Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
			return bitmap;

		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
	/**
	 * 把一个普通的字符串转成MD5格式的字符串
	 *
	 * @param str
	 * @return
	 */
	private static String getMD5(String str) {
		StringBuffer sb = new StringBuffer();

		try{
			//获得摘要对象
			MessageDigest md = MessageDigest.getInstance("md5");
			//转换str--->md5
			md.update(str.getBytes());
			byte[] bytes = md.digest();
			//如下直接转换是可以的，但是可读性太差，不推荐
			//String string = new String(bytes);
			for (byte b : bytes) {
				//把每一个byte数据做一下“格式化”
				// 1111 & 1010--->1010
				String temp = Integer.toHexString(b & 0xFF);
				if(temp.length()==1){
					sb.append("0");
				}
				sb.append(temp);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return sb.toString();
	}
	/*
	 * 安卓下有一个神奇的路径/sys/devices/system
	 * 该路径下有N多个文件用来描述系统的资源
	 * 其中与CPU相关的描述文件都在
	 * /sys/devices/system/cpu路径下面
	 * 如果设备cpu是一个核，它的描述文件就是
	 * /sys/devices/system/cpu/cpu0/XXXXX
	 * 通过判断/sys/devices/system/cpu/下包含的文件数量
	 * 就可以间接知道设备的CPU核数
	 */
	private static int getCoreNumbers() {
		try {
			File file = new File("/sys/devices/system/cpu/");
			File[] files = file.listFiles(new FilenameFilter() {

				@Override
				public boolean accept(File dir, String filename) {
					if(filename.contains("cpu")){
						return true;
					}
					return false;
				}
			});
			return files.length;
		} catch (Exception e) {
			e.printStackTrace();
			return 1;
		}
	}
	/**
	 * 持有bitmap，和要显示bitmap的imageView
	 * @author pjy
	 *
	 */
	private static class TaskBean{
		Bitmap bitmap;
		ImageView iv;
		String tag;//就是bitmap属性对应的下载地址
	}
	/**
	 * 如果返回true，意味着ImageLoader尚未初始化
	 * 如果返回fasle，意味着ImageLoader初始化过了，不需要再次初始化了
	 * @return
	 */
	public static boolean isFirst(){
		return isFirst;
	}
}
