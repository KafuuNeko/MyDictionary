package com.smallcake.mydictionary.struct;

public class Words {
    public String words;
    public String describe;
    public boolean familiar;
    public long wid = -1;

    public Words(String pWords,String pDescribe){
        words = pWords;
        describe = pDescribe;
    }

    public Words(String pWords,String pDescribe,boolean pFamiliar){
        this(pWords, pDescribe);
        familiar = pFamiliar;
    }

    public Words(long pId,String pWords,String pDescribe,boolean pFamiliar){
        this(pWords, pDescribe, pFamiliar);
        wid = pId;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public void setFamiliar(boolean familiar) {
        this.familiar = familiar;
    }

    public void setWId(long id) {
        this.wid = id;
    }

    public String getDescribe() {
        return describe;
    }

    public String getWords() {
        return words;
    }

    public long getWId() {
        return wid;
    }


    @Override
    public String toString() {
        return words;
    }
}
