package org.appland.settlers.assets;

public class Timbre {
    short patch; // uint 8
    short bank; // uint 8

    public Timbre(short patch, short bank) {
        this.patch = patch;
        this.bank = bank;
    }
}
