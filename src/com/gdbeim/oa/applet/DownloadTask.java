package com.gdbeim.oa.applet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


 public class DownloadTask implements Serializable
{
  private static final long serialVersionUID = 3697346132684098095L;
   private List receiveList = new ArrayList(0);

   private List receiveAttachList = new ArrayList(0);
  
   private List sendList = new ArrayList(0);
  



/*  33 */   private List sendAttachList = new ArrayList(0);
  



/*  38 */   private List draftList = new ArrayList(0);
  



/*  43 */   private List draftAttachList = new ArrayList(0);
  



/*  48 */   private List wasteList = new ArrayList(0);
  



/*  53 */   private List wasteAttachList = new ArrayList(0);
  
  public List getReceiveList() {
/*  56 */     return receiveList;
  }
  
  public void setReceiveList(List receiveList) {
/*  60 */     this.receiveList = receiveList;
  }
  
  public List getReceiveAttachList() {
/*  64 */     return receiveAttachList;
  }
  
  public void setReceiveAttachList(List receiveAttachList) {
/*  68 */     this.receiveAttachList = receiveAttachList;
  }
  
  public List getDraftAttachList() {
/*  72 */     return draftAttachList;
  }
  
  public void setDraftAttachList(List draftAttachList) {
/*  76 */     this.draftAttachList = draftAttachList;
  }
  
  public List getDraftList() {
/*  80 */     return draftList;
  }
  
  public void setDraftList(List draftList) {
/*  84 */     this.draftList = draftList;
  }
  
  public List getSendAttachList() {
/*  88 */     return sendAttachList;
  }
  
  public void setSendAttachList(List sendAttachList) {
/*  92 */     this.sendAttachList = sendAttachList;
  }
  
  public List getSendList() {
/*  96 */     return sendList;
  }
  
  public void setSendList(List sendList) {
/* 100 */     this.sendList = sendList;
  }
  
  public List getWasteAttachList() {
/* 104 */     return wasteAttachList;
  }
  
  public void setWasteAttachList(List wasteAttachList) {
/* 108 */     this.wasteAttachList = wasteAttachList;
  }
  
  public List getWasteList() {
/* 112 */     return wasteList;
  }
  
  public void setWasteList(List wasteList) {
/* 116 */     this.wasteList = wasteList;
  }
  
  public long getTotalFileSize() {
/* 120 */     long total = 0L;
    
/* 122 */     for (int i = 0; i < receiveAttachList.size(); i++) {
/* 123 */       Object[] obj = (Object[])receiveAttachList.get(i);
/* 124 */       total += ((Long)obj[3]).longValue();
    }
    
/* 127 */     for (int i = 0; i < sendAttachList.size(); i++) {
/* 128 */       Object[] obj = (Object[])sendAttachList.get(i);
/* 129 */       total += ((Long)obj[3]).longValue();
    }
    
/* 132 */     for (int i = 0; i < draftAttachList.size(); i++) {
/* 133 */       Object[] obj = (Object[])draftAttachList.get(i);
/* 134 */       total += ((Long)obj[3]).longValue();
    }
    
/* 137 */     for (int i = 0; i < wasteAttachList.size(); i++) {
/* 138 */       Object[] obj = (Object[])wasteAttachList.get(i);
/* 139 */       total += ((Long)obj[3]).longValue();
    }
    
/* 142 */     long temp = receiveList.size() + sendList.size() + draftList.size() + wasteList.size();
    
/* 144 */     total += 30720L * temp;
/* 145 */     return total;
  }
  
  public boolean isEmpty() {
/* 149 */     boolean isEmpty = false;
    
/* 151 */     isEmpty = receiveList.size() + receiveAttachList.size() + 
/* 152 */       sendList.size() + sendAttachList.size() + 
/* 153 */       draftList.size() + draftAttachList.size() + 
/* 154 */       wasteList.size() + wasteAttachList.size() == 0;
    
/* 156 */     return isEmpty;
  }
}

/* Location:           C:\Documents and Settings\ls_user\×ÀÃæ\Applet.jar
 * Qualified Name:     com.gdbeim.oa.applet.DownloadTask
 * Java Class Version: 1.2 (46.0)
 * JD-Core Version:    0.7.1
 */