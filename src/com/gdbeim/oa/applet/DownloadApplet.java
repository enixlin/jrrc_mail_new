/*     */ package com.gdbeim.oa.applet;
/*     */ 
/*     */ import java.awt.Color;

/*     */ import java.awt.Font;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.io.File;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.JApplet;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JCheckBox;
/*     */ import javax.swing.JFileChooser;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JProgressBar;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.JTextPane;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DownloadApplet
/*     */   extends JApplet
/*     */   implements ActionListener, ProgressListener
/*     */ {
/*  31 */   private boolean isClear = false;
/*     */   
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*  35 */   private String serverAddress = null;
/*     */   
/*  37 */   private JPanel pane = null;
/*     */   
/*  39 */   private JScrollPane scrolling = null;
/*     */   
/*  41 */   private JTextPane infoBox = null;
/*     */   
/*  43 */   private JTextField directoryField = null;
/*     */   
/*  45 */   private DownloadFeedback feedback = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  51 */   private JButton butLoad = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  56 */   private JButton butChoose = null;
/*     */   
/*     */   private JFileChooser fileChooser;
/*     */   
/*  60 */   private JLabel tipsLabel = null;
/*     */   
/*  62 */   private JLabel direLabel = null;
/*     */   
/*  64 */   private DownloadTask task = null;
/*     */   
/*  66 */   private JProgressBar progressBar = new JProgressBar();
/*     */   
/*  68 */   private JProgressBar currentProgressBar = new JProgressBar();
/*     */   
/*  70 */   private JCheckBox jCheckBox = null;
/*     */   
/*  72 */   private JLabel totalLabel = null;
/*     */   
/*  74 */   private JLabel currentLabel = null;
/*     */   
/*     */ 
/*  77 */   private String strUserId = "";
/*     */   
/*     */ 
/*  80 */   StringBuffer faultInfo = new StringBuffer("下列邮件转移失败,请重新转移或者手工备份:\n");
/*     */   
/*     */   public void init()
/*     */   {
/*     */     try
/*     */     {
/*  86 */       serverAddress = getParameter("serverAddress");
/*     */       
/*  88 */       resize(400, 300);
/*     */       
/*  90 */       if (serverAddress == null) {
/*  91 */         serverAddress = "http://localhost:8081/oa";
/*     */       }
/*     */       
/*  94 */       jbInit();
/*     */     }
/*     */     catch (Exception e) {
/*  97 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   private void jbInit()
/*     */     throws Exception
/*     */   {
/* 104 */     pane = new JPanel();
/* 105 */     pane.setBounds(new Rectangle(0, 0, 500, 325));
/* 106 */     pane.setLayout(null);
/* 107 */     pane.setBorder(BorderFactory.createEtchedBorder(1));
/*     */     
/* 109 */     pane.setBackground(Color.WHITE);
/*     */     
/* 111 */     direLabel = new JLabel("保存到:");
/* 112 */     direLabel.setBounds(new Rectangle(15, 10, 295, 30));
/* 113 */     pane.add(direLabel);
/*     */     
/* 115 */     directoryField = new JTextField();
/*     */     
/* 117 */     directoryField.setBounds(new Rectangle(65, 10, 225, 30));
/* 118 */     pane.add(directoryField);
/*     */     
/* 120 */     butChoose = new JButton();
/* 121 */     butChoose.setBounds(new Rectangle(305, 10, 80, 30));
/* 122 */     butChoose.setText("选择目录");
/* 123 */     butChoose.addActionListener(this);
/* 124 */     pane.add(butChoose);
/*     */     
/*     */ 
/* 127 */     jCheckBox = new JCheckBox();
/* 128 */     jCheckBox.setBounds(new Rectangle(15, 50, 220, 30));
/* 129 */     jCheckBox.setText("下载完成后删除服务器端的邮件");
/* 130 */     jCheckBox.setBackground(Color.WHITE);
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
/* 144 */     pane.add(jCheckBox);
/*     */     
/* 146 */     butLoad = new JButton();
/* 147 */     butLoad.setBounds(new Rectangle(305, 50, 80, 30));
/* 148 */     butLoad.setText("下载");
/* 149 */     butLoad.addActionListener(this);
/* 150 */     pane.add(butLoad);
/*     */     
/*     */ 
/* 153 */     tipsLabel = new JLabel("下载任务统计:");
/* 154 */     tipsLabel.setBounds(new Rectangle(16, 75, 295, 30));
/* 155 */     pane.add(tipsLabel);
/*     */     
/*     */ 
/* 158 */     infoBox = new JTextPane();
/* 159 */     infoBox.setText("");
/* 160 */     infoBox.setEditable(false);
/* 161 */     scrolling = new JScrollPane(infoBox);
/* 162 */     scrolling.setBounds(new Rectangle(15, 105, 370, 120));
/* 163 */     pane.add(scrolling);
/*     */     
/* 165 */     totalLabel = new JLabel("全部进度");
/* 166 */     totalLabel.setBounds(new Rectangle(15, 235, 55, 25));
/* 167 */     pane.add(totalLabel);
/*     */     
/*     */ 
/* 170 */     progressBar.setBounds(new Rectangle(70, 235, 315, 22));
/*     */     
/* 172 */     progressBar.setStringPainted(true);
/* 173 */     progressBar.setMinimum(0);
/* 174 */     pane.add(progressBar);
/*     */     
/*     */ 
/*     */ 
/* 178 */     currentLabel = new JLabel("当前进度");
/* 179 */     currentLabel.setBounds(new Rectangle(15, 265, 55, 25));
/* 180 */     pane.add(currentLabel);
/*     */     
/* 182 */     currentProgressBar.setBounds(new Rectangle(70, 265, 315, 22));
/*     */     
/* 184 */     currentProgressBar.setStringPainted(true);
/* 185 */     currentProgressBar.setMinimum(0);
/* 186 */     pane.add(currentProgressBar);
/*     */     
/*     */ 
/* 189 */     fileChooser = new JFileChooser("d:\\");
/* 190 */     fileChooser.setFileSelectionMode(1);
/* 191 */     fileChooser.setFont(new Font("宋体", 0, 12));
/* 192 */     fileChooser.setDialogTitle("选择邮件保存目录");
/*     */     
/* 194 */     setContentPane(pane);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void getDownLoadList(String startDate, String endDate, String userId)
/*     */   {
/*     */     try
/*     */     {
/* 205 */       strUserId = userId;
/* 206 */       infoBox.setText("请稍候...");
/*     */       
/* 208 */       HttpGet instance = new HttpGet();
/* 209 */       String url = serverAddress + "/messageDownload?method=getDownList" + 
/* 210 */         "&startDate=" + startDate + "&endDate=" + endDate + "&userId=" + userId;
/* 211 */       System.out.println("url=" + url);
/* 212 */       task = instance.getDownloadTask(url);
/*     */       
/* 214 */       displayDownInfo();
/*     */     }
/*     */     catch (Exception err)
/*     */     {
/* 218 */       System.out.println("getDownLoadList error:" + err);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void displayDownInfo()
/*     */   {
/* 229 */     StringBuffer info = new StringBuffer("");
/*     */     
/* 231 */     int emailCout = task.getReceiveList().size() + 
/* 232 */       task.getDraftList().size() + 
/* 233 */       task.getSendList().size() + 
/* 234 */       task.getWasteList().size();
/*     */     
/* 236 */     int attachCount = task.getReceiveAttachList().size() + 
/* 237 */       task.getDraftAttachList().size() + 
/* 238 */       task.getSendAttachList().size() + 
/* 239 */       task.getWasteAttachList().size();
/*     */     
/* 241 */     info.append("收件箱[").append(task.getReceiveList().size())
/* 242 */       .append("]附件[").append(task.getReceiveAttachList().size()).append("]\n")
/* 243 */       .append("发件箱[").append(task.getSendList().size())
/* 244 */       .append("]附件[").append(task.getSendAttachList().size()).append("]\n")
/* 245 */       .append("草稿箱[").append(task.getDraftList().size())
/* 246 */       .append("]附件[").append(task.getDraftAttachList().size()).append("]\n")
/* 247 */       .append("废件箱[").append(task.getWasteList().size())
/* 248 */       .append("]附件[").append(task.getWasteAttachList().size()).append("]\n")
/* 249 */       .append("共计邮件[").append(emailCout)
/* 250 */       .append("]封,附件[").append(attachCount).append("]个.\n");
/*     */     
/* 252 */     info.append("需要磁盘空间:[").append(task.getTotalFileSize() / 1048576L).append("]MB");
/*     */     
/* 254 */     infoBox.setText(info.toString());
/*     */   }
/*     */   
/*     */ 
/*     */   public void actionPerformed(ActionEvent e)
/*     */   {
/* 260 */     if (e.getSource() == butLoad)
/*     */     {
/*     */ 
/* 263 */       if ((task == null) || (task.isEmpty())) {
/* 264 */         JOptionPane.showMessageDialog(pane, "下载列表为空");
/* 265 */         return;
/*     */       }
/*     */       
/* 268 */       if ("".equals(directoryField.getText().trim()))
/*     */       {
/* 270 */         JOptionPane.showMessageDialog(pane, "请指定邮件保存目录!");
/* 271 */         return;
/*     */       }
/*     */       
/* 274 */       if (!DiskTool.checkPathExists(directoryField.getText())) {
/* 275 */         JOptionPane.showMessageDialog(pane, "邮件保存目录不存在!");
/* 276 */         return;
/*     */       }
/*     */       
/* 279 */       long emailSpace = task.getTotalFileSize();
/*     */       
/* 281 */       System.out.println("emailSpace:" + emailSpace);
/*     */       
/* 283 */       long dirSpace = DiskTool.getFreeDiskSpace(directoryField.getText());
/*     */       
/* 285 */       System.out.println("dirSpace:" + dirSpace);
/*     */       
/* 287 */       long restSpace = dirSpace - emailSpace;
/* 288 */       if (dirSpace == -1L) {
/* 289 */         int value = JOptionPane.showConfirmDialog(pane, "检查磁盘空间失败,请确保磁盘空间足够!", "提示", 0);
/* 290 */         if (value == 1) {
/* 291 */           return;
/*     */         }
/*     */       }
/* 294 */       else if (restSpace < 0L) {
/* 295 */         JOptionPane.showMessageDialog(pane, "所选目录空间不足,请重新选择!");
/* 296 */         return;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 301 */       if ((dirSpace != -1L) && (directoryField.getText().startsWith("C")) && 
/* 302 */         (restSpace < 209715200L)) {
/* 303 */         JOptionPane.showMessageDialog(pane, "下载完成后C盘空间小于200MB,请重新选择!");
/* 304 */         return;
/*     */       }
/*     */       
/* 307 */       if (jCheckBox.isSelected()) {
/* 308 */         JOptionPane.showMessageDialog(pane, "邮件备份到本地后,服务端的对应邮件将被清除,请到本地文件浏览.");
/*     */       }
/*     */       
/* 311 */       butLoad.setEnabled(false);
/*     */       
/* 313 */       doDownload();
/*     */     }
/* 315 */     else if (e.getSource() == butChoose)
/*     */     {
/*     */ 
/* 318 */       fileChooser.showSaveDialog(this);
/* 319 */       if (fileChooser.getSelectedFile() != null) {
/* 320 */         directoryField.setText(fileChooser.getSelectedFile().getPath());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void doDownload()
/*     */   {
/* 333 */     HttpGet instance = new HttpGet();
/*     */     
/*     */ 
/*     */ 
/* 337 */     Long mailSize = new Long(4096L);
/*     */     
/* 339 */     feedback = new DownloadFeedback();
/*     */     
/*     */ 
/* 342 */     List temp = task.getReceiveList();
/* 343 */     String filePath = DiskTool.createFolder(directoryField.getText() + "/收件箱");
/* 344 */     for (int i = 0; i < temp.size(); i++) {
/* 345 */       Object[] obj = (Object[])temp.get(i);
/* 346 */       String url = serverAddress + "/messageDownload?method=getContent&messageid=" + obj[0];
/*     */       
/* 348 */       List attachIds = new ArrayList();
/* 349 */       long attachSize = 0L;
/* 350 */       List temp2 = task.getReceiveAttachList();
/* 351 */       for (int j = 0; j < temp2.size(); j++) {
/* 352 */         Object[] obj2 = (Object[])temp2.get(j);
/*     */         
/* 354 */         Long id = (Long)obj2[4];
/* 355 */         String attachName = (String)obj2[1];
/* 356 */         Long aSize = (Long)obj2[3];
/* 357 */         if (id.longValue() == ((Long)obj[0]).longValue()) {
/* 358 */           String attachUrl = serverAddress + "//messageFileDown?id=" + obj2[0] + ";" + 
/* 359 */             DiskTool.createFolder(new StringBuffer(String.valueOf(filePath)).append("/附件").toString()) + "/" + attachName;
/* 360 */           attachIds.add(attachUrl);
/* 361 */           attachSize += aSize.longValue();
/*     */         }
/*     */       }
/* 364 */       instance.addItem(url, filePath + "/" + obj[1] + ".html", 
/* 365 */         new Long(mailSize.longValue() + attachSize), "receive", attachIds);
/*     */       
/* 367 */       feedback.addReceive((Long)obj[0]);
/*     */     }
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
/* 380 */     temp = task.getSendList();
/* 381 */     filePath = DiskTool.createFolder(directoryField.getText() + "/发件箱");
/* 382 */     for (int i = 0; i < temp.size(); i++) {
/* 383 */       Object[] obj = (Object[])temp.get(i);
/* 384 */       String url = serverAddress + "/messageDownload?method=getContent&messageid=" + obj[0];
/*     */       
/*     */ 
/* 387 */       List attachIds = new ArrayList();
/* 388 */       long attachSize = 0L;
/* 389 */       List temp2 = task.getSendAttachList();
/* 390 */       for (int j = 0; j < temp2.size(); j++) {
/* 391 */         Object[] obj2 = (Object[])temp2.get(j);
/* 392 */         Long id = (Long)obj2[4];
/* 393 */         String attachName = (String)obj2[1];
/* 394 */         Long aSize = (Long)obj2[3];
/* 395 */         if (id.longValue() == ((Long)obj[0]).longValue()) {
/* 396 */           String attachUrl = serverAddress + "//messageFileDown?id=" + obj2[0] + ";" + 
/* 397 */             DiskTool.createFolder(new StringBuffer(String.valueOf(filePath)).append("/附件").toString()) + "/" + attachName;
/* 398 */           attachIds.add(attachUrl);
/* 399 */           attachSize += aSize.longValue();
/*     */         }
/*     */       }
/* 402 */       instance.addItem(url, filePath + "/" + obj[1] + ".html", 
/* 403 */         new Long(mailSize.longValue() + attachSize), "send", attachIds);
/*     */       
/* 405 */       feedback.addSend((Long)obj[0]);
/*     */     }
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
/* 418 */     temp = task.getDraftList();
/* 419 */     filePath = DiskTool.createFolder(directoryField.getText() + "/草稿箱");
/* 420 */     for (int i = 0; i < temp.size(); i++) {
/* 421 */       Object[] obj = (Object[])temp.get(i);
/* 422 */       String url = serverAddress + "/messageDownload?method=getContent&messageid=" + obj[0];
/*     */       
/*     */ 
/* 425 */       List attachIds = new ArrayList();
/* 426 */       long attachSize = 0L;
/* 427 */       List temp2 = task.getDraftAttachList();
/* 428 */       for (int j = 0; j < temp2.size(); j++) {
/* 429 */         Object[] obj2 = (Object[])temp2.get(j);
/* 430 */         Long id = (Long)obj2[4];
/* 431 */         String attachName = (String)obj2[1];
/* 432 */         Long aSize = (Long)obj2[3];
/* 433 */         if (id.longValue() == ((Long)obj[0]).longValue()) {
/* 434 */           String attachUrl = serverAddress + "//messageFileDown?id=" + obj2[0] + ";" + 
/* 435 */             DiskTool.createFolder(new StringBuffer(String.valueOf(filePath)).append("/附件").toString()) + "/" + attachName;
/* 436 */           attachIds.add(attachUrl);
/* 437 */           attachSize += aSize.longValue();
/*     */         }
/*     */       }
/* 440 */       instance.addItem(url, filePath + "/" + obj[1] + ".html", 
/* 441 */         new Long(mailSize.longValue() + attachSize), "draft", attachIds);
/*     */       
/* 443 */       feedback.addDraft((Long)obj[0]);
/*     */     }
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
/* 456 */     temp = task.getWasteList();
/* 457 */     long attachSize = 0L;
/* 458 */     filePath = DiskTool.createFolder(directoryField.getText() + "/废件箱");
/* 459 */     for (int i = 0; i < temp.size(); i++) {
/* 460 */       Object[] obj = (Object[])temp.get(i);
/* 461 */       String url = serverAddress + "/messageDownload?method=getContent&messageid=" + obj[0];
/*     */       
/*     */ 
/* 464 */       List attachIds = new ArrayList();
/* 465 */       List temp2 = task.getWasteAttachList();
/* 466 */       for (int j = 0; j < temp2.size(); j++) {
/* 467 */         Object[] obj2 = (Object[])temp2.get(j);
/* 468 */         Long id = (Long)obj2[4];
/* 469 */         String attachName = (String)obj2[1];
/* 470 */         Long aSize = (Long)obj2[3];
/* 471 */         if (id.longValue() == ((Long)obj[0]).longValue()) {
/* 472 */           String attachUrl = serverAddress + "//messageFileDown?id=" + obj2[0] + ";" + 
/* 473 */             DiskTool.createFolder(new StringBuffer(String.valueOf(filePath)).append("/附件").toString()) + "/" + attachName;
/* 474 */           attachIds.add(attachUrl);
/* 475 */           attachSize += aSize.longValue();
/*     */         }
/*     */       }
/* 478 */       instance.addItem(url, filePath + "/" + obj[1] + ".html", 
/* 479 */         new Long(mailSize.longValue() + attachSize), "waste", attachIds);
/*     */       
/* 481 */       feedback.addWaste((Long)obj[0]);
/*     */     }
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
/* 494 */     instance.downLoadByList(this);
/*     */   }
/*     */   
/*     */ 
/*     */   public void deleteFeedBack(boolean isSuccess, String type, Long id)
/*     */   {
/* 500 */     if ((isSuccess) && (jCheckBox.isSelected())) {
/* 501 */       HttpGet instance = new HttpGet();
/* 502 */       DownloadFeedback fd = new DownloadFeedback();
/*     */       
/* 504 */       if ("receive".equals(type)) {
/* 505 */         fd.addReceive(id);
/*     */       }
/* 507 */       if ("send".equals(type)) {
/* 508 */         fd.addSend(id);
/*     */       }
/* 510 */       if ("draft".equals(type)) {
/* 511 */         fd.addDraft(id);
/*     */       }
/* 513 */       if ("waste".equals(type)) {
/* 514 */         fd.addWaste(id);
/*     */       }
/* 516 */       instance.sendFeedBack(serverAddress + "/messageDownload?method=feedBack&userId=" + strUserId, fd);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void displayFaultInfo()
/*     */   {
/* 524 */     infoBox.setText(faultInfo.toString());
/* 525 */     faultInfo = new StringBuffer("下列邮件转移失败,请重新转移或者手工备份:\n");
/*     */   }
/*     */   
/*     */   public boolean fault(String type, Long id, String message)
/*     */   {
/* 530 */     String msg = "邮件标题为: \"" + message + "\" 的邮件备份失败,是否继续备份其他邮件?";
/* 531 */     if (pane != null) {
/* 532 */       int value = JOptionPane.showConfirmDialog(pane, msg, "提示", 0);
/*     */       
/* 534 */       StringBuffer msgError = new StringBuffer("");
/* 535 */       if ("receive".equals(type)) {
/* 536 */         msgError.append("收件箱:");
/*     */       }
/* 538 */       if ("send".equals(type)) {
/* 539 */         msgError.append("发件箱:");
/*     */       }
/* 541 */       if ("draft".equals(type)) {
/* 542 */         msgError.append("草稿箱:");
/*     */       }
/* 544 */       if ("waste".equals(type)) {
/* 545 */         msgError.append("废件箱:");
/*     */       }
/* 547 */       msgError.append("邮件ID(").append(id.longValue()).append("):邮件标题(").append(message).append(")\n");
/* 548 */       faultInfo.append(msgError);
/* 549 */       if (value == 1) {
/* 550 */         return false;
/*     */       }
/*     */     }
/* 553 */     return true;
/*     */   }
/*     */   
/*     */   public void showProgress(int current, int total)
/*     */   {
/* 558 */     progressBar.setMaximum(total);
/*     */     
/* 560 */     progressBar.setValue(current);
/*     */   }
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
/*     */   public void finish(boolean isSuccess)
/*     */   {
/* 576 */     task = null;
/*     */     
/* 578 */     if ((isSuccess) && (jCheckBox.isSelected()))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 583 */       isClear = true;
/*     */     }
/*     */     
/*     */ 
/* 587 */     if ((isSuccess) && (progressBar.getValue() == progressBar.getMaximum()))
/*     */     {
/* 589 */       currentProgressBar.setValue(currentProgressBar.getMaximum());
/*     */       
/* 591 */       JOptionPane.showMessageDialog(pane, "邮件已成功转移到本地!");
/*     */     } else {
/* 593 */       JOptionPane.showMessageDialog(pane, "邮件备份部分成功,请重新备份!");
/* 594 */       displayFaultInfo();
/*     */     }
/*     */     
/* 597 */     butLoad.setEnabled(true);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isClear()
/*     */   {
/* 603 */     return isClear;
/*     */   }
/*     */   
/*     */   public void setCurrentProgress(int total)
/*     */   {
/* 608 */     currentProgressBar.setValue(0);
/* 609 */     currentProgressBar.setMaximum(total);
/*     */   }
/*     */   
/*     */   public void showCurrentProgress(int increaseSize)
/*     */   {
/* 614 */     currentProgressBar.setValue(currentProgressBar.getValue() + increaseSize);
/*     */   }
/*     */ }

/* Location:           C:\Documents and Settings\ls_user\桌面\Applet.jar
 * Qualified Name:     com.gdbeim.oa.applet.DownloadApplet
 * Java Class Version: 1.2 (46.0)
 * JD-Core Version:    0.7.1
 */