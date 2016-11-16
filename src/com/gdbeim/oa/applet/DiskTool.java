/*     */ package com.gdbeim.oa.applet;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintStream;
/*     */ 

/*     */ 
/*     */ public class DiskTool
/*     */ {
/*     */   public static long getFreeDiskSpace(String dirName)
/*     */   {
/*     */     try
/*     */     {
/*  30 */       String temp = "";
/*  31 */       if (dirName.indexOf(":") > 0) {
/*  32 */         temp = dirName.substring(0, dirName.indexOf(":") + 1);
/*     */       }
/*     */       
/*  35 */       System.out.println(temp);
/*     */       
/*  37 */       String os = System.getProperty("os.name");
/*     */       
/*  39 */       String command; if (os.startsWith("Windows")) {
/*  40 */         command = "cmd.exe /c dir " + temp;
/*     */       } else {
/*  42 */         command = "command.com /c dir " + temp;
/*     */       }
/*     */       
/*  45 */       Runtime runtime = Runtime.getRuntime();
/*  46 */       Process process = null;
/*  47 */       process = runtime.exec(command);
/*  48 */       if (process == null) {
/*  49 */         return -1L;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*  54 */       BufferedReader in = new BufferedReader(new InputStreamReader(
/*  55 */         process.getInputStream()));
/*     */       
/*  57 */       String freeSpace = null;
/*     */       String line;
/*  59 */       while ((line = in.readLine()) != null) { 
				
/*  60 */         freeSpace = line;
/*     */       }
/*     */       
/*  63 */       if (freeSpace == null) {
/*  64 */         System.out.println("freeSpace is null");
/*  65 */         return -1L;
/*     */       }
/*     */       
/*  68 */       process.destroy();
/*     */       
/*  70 */       freeSpace = freeSpace.trim();
/*  71 */       freeSpace = freeSpace.replaceAll("\\.", "");
/*  72 */       freeSpace = freeSpace.replaceAll(",", "");
/*  73 */       String[] items = freeSpace.split(" ");
/*     */       
/*     */ 
/*  76 */       int index = 1;
/*  77 */       while (index < items.length) {
/*     */         try {
/*  79 */           return Long.parseLong(items[(index++)]);
/*     */         }
/*     */         catch (NumberFormatException localNumberFormatException) {}
/*     */       }
/*     */       
/*  84 */       System.out.println("testing is -1");
/*  85 */       return -1L;
/*     */     } catch (Exception exception) {
/*  87 */       System.out.println("exception:" + exception); }
/*  88 */     return -1L;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/* 111 */     System.out.println(getFreeDiskSpace("d:"));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String createFolder(String folderPath)
/*     */   {
/* 121 */     String txt = folderPath;
/*     */     try {
/* 123 */       File myFilePath = new File(txt);
/* 124 */       txt = folderPath;
/* 125 */       if (!myFilePath.exists()) {
/* 126 */         myFilePath.mkdir();
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/* 130 */       System.out.println("创建目录操作出错");
/*     */     }
/* 132 */     return txt;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean checkPathExists(String folderPath)
/*     */   {
/* 140 */     boolean isExists = false;
/*     */     try {
/* 142 */       File myFilePath = new File(folderPath);
/* 143 */       isExists = myFilePath.exists();
/*     */     }
/*     */     catch (Exception e) {
/* 146 */       System.out.println("目录检查出错");
/*     */     }
/* 148 */     return isExists;
/*     */   }
/*     */ }

/* Location:           C:\Documents and Settings\ls_user\桌面\Applet.jar
 * Qualified Name:     com.gdbeim.oa.applet.DiskTool
 * Java Class Version: 1.2 (46.0)
 * JD-Core Version:    0.7.1
 */