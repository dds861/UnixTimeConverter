package com.mathius.dd.milestokm;

/**
 * Created by dds86 on 20-Feb-18.
 */

public class Model {
    private String textView1, textView2, edinica1, edinica2;

    public Model(String textView1, String textView2, String edinica1, String edinica2) {
        this.textView1 = textView1;
        this.textView2 = textView2;
        this.edinica1 = edinica1;
        this.edinica2 = edinica2;
    }

    public String getTextView1() {
        return textView1;
    }

    public void setTextView1(String textView1) {
        this.textView1 = textView1;
    }

    public String getTextView2() {
        return textView2;
    }

    public void setTextView2(String textView2) {
        this.textView2 = textView2;
    }

    public String getEdinica1() {
        return edinica1;
    }

    public void setEdinica1(String edinica1) {
        this.edinica1 = edinica1;
    }

    public String getEdinica2() {
        return edinica2;
    }

    public void setEdinica2(String edinica2) {
        this.edinica2 = edinica2;
    }
}
