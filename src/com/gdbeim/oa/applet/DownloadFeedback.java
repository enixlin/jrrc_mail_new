/*    */ package com.gdbeim.oa.applet;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DownloadFeedback
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 5548425631895588265L;
/* 18 */   private List receiveList = new ArrayList();
/*    */   
/* 20 */   private List sendList = new ArrayList();
/*    */   
/* 22 */   private List draftList = new ArrayList();
/*    */   
/* 24 */   private List wasteList = new ArrayList();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void addReceive(Long messageid)
/*    */   {
/* 31 */     receiveList.add(messageid);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void addSend(Long messageid)
/*    */   {
/* 38 */     sendList.add(messageid);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void addDraft(Long messageid)
/*    */   {
/* 45 */     draftList.add(messageid);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void addWaste(Long messageid)
/*    */   {
/* 52 */     wasteList.add(messageid);
/*    */   }
/*    */   
/*    */   public Long[] getReceiveIds() {
/* 56 */     Long[] result = new Long[receiveList.size()];
/* 57 */     for (int i = 0; i < receiveList.size(); i++) {
/* 58 */       result[i] = ((Long)receiveList.get(i));
/*    */     }
/* 60 */     return result;
/*    */   }
/*    */   
/*    */   public Long[] getSendIds() {
/* 64 */     Long[] result = new Long[sendList.size()];
/* 65 */     for (int i = 0; i < sendList.size(); i++) {
/* 66 */       result[i] = ((Long)sendList.get(i));
/*    */     }
/* 68 */     return result;
/*    */   }
/*    */   
/*    */   public Long[] getDraftIds() {
/* 72 */     Long[] result = new Long[draftList.size()];
/* 73 */     for (int i = 0; i < draftList.size(); i++) {
/* 74 */       result[i] = ((Long)draftList.get(i));
/*    */     }
/* 76 */     return result;
/*    */   }
/*    */   
/*    */   public Long[] getWasteIds() {
/* 80 */     Long[] result = new Long[wasteList.size()];
/* 81 */     for (int i = 0; i < wasteList.size(); i++) {
/* 82 */       result[i] = ((Long)wasteList.get(i));
/*    */     }
/* 84 */     return result;
/*    */   }
/*    */ }

/* Location:           C:\Documents and Settings\ls_user\×ÀÃæ\Applet.jar
 * Qualified Name:     com.gdbeim.oa.applet.DownloadFeedback
 * Java Class Version: 1.2 (46.0)
 * JD-Core Version:    0.7.1
 */