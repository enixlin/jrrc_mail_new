/*     */package com.gdbeim.oa.applet;

/*     */
/*     */import java.io.BufferedInputStream;
/*     */
import java.io.BufferedReader;
/*     */
import java.io.FileOutputStream;
/*     */
import java.io.IOException;
/*     */
import java.io.InputStreamReader;
/*     */
import java.io.ObjectInputStream;
/*     */
import java.io.PrintStream;
/*     */
import java.net.HttpURLConnection;
/*     */
import java.net.URL;
/*     */
import java.text.DecimalFormat;
/*     */
import java.util.ArrayList;

/*     */
import java.util.List;
/*     */
import java.util.Vector;

/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */public class HttpGet
/*     */{
	/*     */public static final boolean DEBUG = false;
	/* 26 */private static int BUFFER_SIZE = 8192;
	/*     */
	/* 28 */private Vector vDownLoad = new Vector();
	/*     */
	/* 30 */private Vector vFileList = new Vector();
	/*     */
	/* 32 */private Vector vFileSize = new Vector();
	/*     */
	/* 34 */private Vector vType = new Vector();
	/*     */
	/* 36 */private Vector vAttachList = new Vector();

	/*     */
	/*     */
	/*     */
	/*     */
	/*     */
	/*     */
	/*     */
	/*     */
	/*     */
	/*     */
	/*     */
	/*     */
	/*     */
	/*     */
	/*     */public void resetList()
	/*     */{
		/* 53 */vDownLoad.clear();
		/* 54 */vFileList.clear();
		/*     */}

	/*     */
	/*     */
	/*     */
	/*     */
	/*     */
	/*     */
	/*     */
	/*     */
	/*     */public void addItem(String url, String filename, Long filesize,
			String type, List attachIds)
	/*     */{
		/* 66 */vDownLoad.add(url);
		/* 67 */vFileList.add(filename);
		/* 68 */vFileSize.add(filesize);
		/* 69 */vType.add(type);
		/* 70 */vAttachList.add(attachIds);
		/*     */}

	/*     */
	/*     */
	/*     */
	/*     */
	/*     */public void downLoadByList(ProgressListener listener)
	/*     */{
		/* 78 */Thread thread = new Thread(new Runnable() {
			/*     */private final ProgressListener val$listener=null;

			/*     */
			/* 81 */public void run() {
				int total = vDownLoad.size();
				/* 82 */boolean isSuccess = false;
				/*     */
				/* 84 */int cnt = 0;
				/*     */try
				/*     */{
					/* 87 */String url = "";
					/* 88 */String filename = "";
					/* 89 */String type = "";
					/* 90 */List lstAttach = new ArrayList();
					/* 91 */Long id = new Long(0L);
					/* 92 */for (int i = 0; i < total; i++) {
						/* 93 */url = (String) vDownLoad.get(i);
						/* 94 */filename = (String) vFileList.get(i);
						/* 95 */type = (String) vType.get(i);
						/* 96 */lstAttach = (List) vAttachList.get(i);
						/* 97 */int fileSize = ((Long) vFileSize.get(i))
								.intValue();
						/* 98 */val$listener.setCurrentProgress(fileSize);
						/*     */
						/*     */try
						/*     */{
							/* 102 */isSuccess = saveMessage(url, filename,
									val$listener, lstAttach);
							/*     */
							/* 104 */id = new Long(url.substring(
									url.lastIndexOf("id=") + 3, url.length()));
							/* 105 */if (isSuccess) {
								/* 106 */val$listener.deleteFeedBack(true,
										type, id);
								/* 107 */cnt++;
								/*     */} else {
								/* 109 */String title = filename.substring(
										filename.indexOf("]") + 1,
										filename.lastIndexOf("."));
								/* 110 */if (!val$listener.fault(type, id,
										title))
									/*     */break;
								/*     */}
							/* 113 */if (i % 10 == 0)
								Thread.sleep(2000L);
							else
								/* 114 */Thread.sleep(200L);
							/*     */} catch (Exception e) {
							/* 116 */System.out.println(e);
							/* 117 */isSuccess = false;
							/* 118 */String title = filename.substring(
									filename.indexOf("]") + 1,
									filename.lastIndexOf("."));
							/* 119 */if (val$listener.fault(type, id, title)) {
								break ;
							}
							break;
						}
						/*     */
						/*     */label347:
						/* 122 */val$listener.showProgress(i + 1, total);
						/*     */}
					/*     */}
				/*     */catch (Exception e)
				/*     */{
					/* 127 */isSuccess = false;
					/* 128 */System.out.println(e);
					/*     */}
				/*     */
				/*     */
				/*     */
				/*     */
				/* 134 */val$listener.finish(cnt == total);
				/*     */
				/*     */}
			/*     */
			/*     */
			/* 139 */
		});
		/* 140 */thread.start();
		/*     */}


	/*     */public boolean saveToFile(String destUrl, String fileName,
			ProgressListener listener)
	/*     */throws IOException, InterruptedException
	/*     */{
		/* 159 */boolean isSuccess = true;
		/*     */
		/* 161 */FileOutputStream fos = null;
		/* 162 */BufferedInputStream bis = null;
		/* 163 */HttpURLConnection httpUrl = null;
		/* 164 */URL url = null;
		/* 165 */byte[] buf = new byte[BUFFER_SIZE];
		/* 166 */int size = 0;
		/*     */try
		/*     */{
			/* 169 */url = new URL(destUrl);
			/* 170 */httpUrl = (HttpURLConnection) url.openConnection();
			/*     */
			/* 172 */httpUrl.connect();
			/*     */
			/* 174 */bis = new BufferedInputStream(httpUrl.getInputStream());
			/*     */
			/* 176 */fos = new FileOutputStream(fileName);
			/*     */
			/*     */
			/*     */
			/*     */
			/*     */
			/*     */
			/* 183 */while ((size = bis.read(buf)) != -1) {
				/* 184 */fos.write(buf, 0, size);
				/* 185 */listener.showCurrentProgress(size);
				/*     */}
			/*     */
			/* 188 */fos.close();
			/* 189 */bis.close();
			/* 190 */httpUrl.disconnect();
			/*     */
			/* 192 */Thread.sleep(10L);
			/*     */}
		/*     */catch (IOException e)
		/*     */{
			/* 196 */isSuccess = false;
			/* 197 */throw e;
			/*     */} catch (InterruptedException e) {
			/* 199 */isSuccess = false;
			/* 200 */throw e;
			/*     */}
		/*     */
		/* 203 */return isSuccess;
		/*     */}

	/*     */
	/*     */public boolean saveMessage(String destUrl, String fileName,
			ProgressListener listener, List lstAttach)
	/*     */throws IOException, InterruptedException
	/*     */{
		/* 209 */boolean isSuccess = false;
		/*     */
		/*     */
		/* 212 */isSuccess = saveToFile(destUrl, fileName, listener);
		/*     */
		/* 214 */if (!isSuccess) {
			/* 215 */return false;
			/*     */}
		/*     */
		/*     */
		/* 219 */for (int i = 0; i < lstAttach.size(); i++) {
			/* 220 */String attachUrl = (String) lstAttach.get(i);
			/* 221 */String[] aUrl = attachUrl.split(";");
			/* 222 */if ((aUrl != null) && (aUrl.length == 2)) {
				/* 223 */String url = aUrl[0];
				/* 224 */String filename = aUrl[1];
				/*     */
				/* 226 */isSuccess = saveToFile(url, filename, listener);
				/* 227 */if (!isSuccess) {
					return false;
					/*     */}
				/*     */}
			/*     */}
		/*     */
		/*     */
		/*     */
		/* 234 */return isSuccess;
		/*     */}

	/*     */
	/*     */
	/*     */
	/*     */
	/*     */
	/*     */public DownloadTask getDownloadTask(String strUrl)
	/*     */{
		/* 243 */HttpURLConnection httpUrl = null;
		/* 244 */URL url = null;
		/*     */
		/* 246 */DownloadTask task = null;
		/*     */try
		/*     */{
			/* 249 */url = new URL(strUrl);
			/* 250 */httpUrl = (HttpURLConnection) url.openConnection();
			/*     */
			/* 252 */httpUrl.connect();
			/*     */
			/* 254 */ObjectInputStream ois = new ObjectInputStream(
					httpUrl.getInputStream());
			/*     */
			/* 256 */task = (DownloadTask) ois.readObject();
			/* 257 */ois.close();
			/*     */
			/* 259 */httpUrl.disconnect();
			/*     */}
		/*     */catch (Exception e) {
			/* 262 */System.out.println("DownloadTask error:" + e);
			/*     */}
		/*     */
		/* 265 */return task;
		/*     */}

	/*     */
	/*     */
	/*     */
	/*     */
	/*     */
	/*     */public void sendFeedBack(String strUrl, DownloadFeedback feed)
	/*     */{
		/* 274 */HttpURLConnection httpConn = null;
		/* 275 */URL url = null;
		/*     */
		/*     */
		/*     */
		/*     */
		/*     */try
		/*     */{
			/* 282 */StringBuffer info = new StringBuffer();
			/*     */
			/* 284 */info.append("&receiveIds=");
			/* 285 */Long[] receiveIds = feed.getReceiveIds();
			/* 286 */for (int i = 0; i < receiveIds.length; i++) {
				/* 287 */info.append(receiveIds[i]).append(",");
				/*     */}
			/*     */
			/* 290 */if (receiveIds.length > 0) {
				/* 291 */info.deleteCharAt(info.length() - 1);
				/*     */}
			/*     */
			/* 294 */info.append("&sendIds=");
			/* 295 */Long[] sendIds = feed.getSendIds();
			/* 296 */for (int i = 0; i < sendIds.length; i++) {
				/* 297 */info.append(sendIds[i]).append(",");
				/*     */}
			/*     */
			/* 300 */if (sendIds.length > 0) {
				/* 301 */info.deleteCharAt(info.length() - 1);
				/*     */}
			/*     */
			/* 304 */info.append("&draftIds=");
			/* 305 */Long[] draftIds = feed.getDraftIds();
			/* 306 */for (int i = 0; i < draftIds.length; i++) {
				/* 307 */info.append(draftIds[i]).append(",");
				/*     */}
			/*     */
			/* 310 */if (draftIds.length > 0) {
				/* 311 */info.deleteCharAt(info.length() - 1);
				/*     */}
			/*     */
			/* 314 */info.append("&wasteIds=");
			/* 315 */Long[] wasteIds = feed.getWasteIds();
			/* 316 */for (int i = 0; i < wasteIds.length; i++) {
				/* 317 */info.append(wasteIds[i]).append(",");
				/*     */}
			/*     */
			/* 320 */if (wasteIds.length > 0) {
				/* 321 */info.deleteCharAt(info.length() - 1);
				/*     */}
			/*     */
			/*     */
			/*     */
			/* 326 */url = new URL(strUrl + info);
			/*     */
			/* 328 */httpConn = (HttpURLConnection) url.openConnection();
			/*     */
			/* 330 */httpConn.setDoOutput(true);
			/*     */
			/*     */
			/*     */
			/*     */
			/*     */
			/* 336 */httpConn.connect();
			/*     */
			/*     */
			/*     */
			/*     */
			/*     */
			/* 342 */httpConn.connect();
			/*     */
			/* 344 */BufferedReader reader = new BufferedReader(
					new InputStreamReader(httpConn.getInputStream()));
			/*     */
			/*     */
			/*     */String line;
			/*     */
			/*     */
			/* 350 */while ((line = reader.readLine()) != null) {
				/* 351 */System.out.println(line);
				/*     */}
			/*     */
			/*     */
			/*     */
			/* 356 */reader.close();
			/*     */
			/* 358 */httpConn.disconnect();
			/*     */}
		/*     */catch (Exception e) {
			/* 361 */e.printStackTrace();
			/*     */}
		/*     */}

	/*     */
	/*     */
	/*     */
	/*     */
	/*     */
	/*     */
	/*     */
	/*     */
	/*     */public static void main(String[] argv)
	/*     */{
		/* 374 */HttpGet instance = new HttpGet();
		/*     */
		/*     */
		/*     */
		/*     */
		/*     */
		/*     */
		/*     */try
		/*     */{
			/* 383 */instance.downLoadByList(null);
			/*     */
			/*     */}
		/*     */catch (Exception err)
		/*     */{
			/*     */
			/* 389 */err.printStackTrace();
			/* 390 */System.out.println(err.getMessage());
			/*     */}
		/*     */}

	/*     */
	/*     */public String getSpeed(double s)
	/*     */{
		/* 396 */String size = "";
		/* 397 */if (s > 1024.0D) {
			/* 398 */s /= 1024.0D;
			/* 399 */size = new DecimalFormat("#,##0.00").format(s) + "MB";
			/*     */} else {
			/* 401 */size = new DecimalFormat("#,##0.00").format(s) + "KB";
			/*     */}
		/*     */
		/*     */
		/*     */
		/*     */
		/* 407 */return size;
		/*     */}

	/*     */
	/*     */public String getTime(long ms) {
		/* 411 */int ss = 1000;
		/* 412 */int mi = ss * 60;
		/* 413 */int hh = mi * 60;
		/* 414 */int dd = hh * 24;
		/*     */
		/* 416 */long day = ms / dd;
		/* 417 */long hour = (ms - day * dd) / hh;
		/* 418 */long minute = (ms - day * dd - hour * hh) / mi;
		/* 419 */long second = (ms - day * dd - hour * hh - minute * mi) / ss;
		/* 420 */long milliSecond = ms - day * dd - hour * hh - minute * mi
				- second * ss;
		/*     */
		/* 422 */String strDay = String.valueOf(day);
		/* 423 */String strHour = String.valueOf(hour);
		/* 424 */String strMinute = String.valueOf(minute);
		/* 425 */String strSecond = String.valueOf(second);
		/* 426 */String strMilliSecond = String.valueOf(milliSecond);
		/* 427 */strMilliSecond = strMilliSecond;
		/* 428 */if (day > 0L) {
			/* 429 */return strDay + "��" + strHour + "ʱ" + strMinute + "��"
					+ strSecond + "��";
			/*     */}
		/* 431 */if (hour > 0L) {
			/* 432 */return strHour + "ʱ" + strMinute + "��" + strSecond + "��";
			/*     */}
		/* 434 */if (minute > 0L) {
			/* 435 */return strMinute + "��" + strSecond + "��";
			/*     */}
		/* 437 */if (second > 0L) {
			/* 438 */return strSecond + "��";
			/*     */}
		/* 440 */if (milliSecond > 0L) {
			/* 441 */return strMilliSecond + "����";
			/*     */}
		/* 443 */return ms + "����";
		/*     */}
	/*     */
}

/*
 * Location: C:\Documents and Settings\ls_user\����\Applet.jar Qualified Name:
 * com.gdbeim.oa.applet.HttpGet Java Class Version: 1.2 (46.0) JD-Core Version:
 * 0.7.1
 */