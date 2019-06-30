package com.latico.commons.aspectj;

public class Speaker {
    public void speak() {
        System.out.println("[Speaker] bla bla ");
    }

    public static void main(String[] args) {
        Speaker speaker = new Speaker();
        speaker.speak();
    }
}