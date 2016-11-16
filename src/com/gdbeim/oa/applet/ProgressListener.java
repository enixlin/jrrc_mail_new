package com.gdbeim.oa.applet;

public abstract interface ProgressListener
{
  public abstract void showProgress(int paramInt1, int paramInt2);
  
  public abstract void finish(boolean paramBoolean);
  
  public abstract void setCurrentProgress(int paramInt);
  
  public abstract void showCurrentProgress(int paramInt);
  
  public abstract boolean fault(String paramString1, Long paramLong, String paramString2);
  
  public abstract void deleteFeedBack(boolean paramBoolean, String paramString, Long paramLong);
}

/* Location:           C:\Documents and Settings\ls_user\×ÀÃæ\Applet.jar
 * Qualified Name:     com.gdbeim.oa.applet.ProgressListener
 * Java Class Version: 1.2 (46.0)
 * JD-Core Version:    0.7.1
 */