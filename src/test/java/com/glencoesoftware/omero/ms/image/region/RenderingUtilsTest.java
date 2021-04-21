/*
 * Copyright (C) 2021 Glencoe Software, Inc. All rights reserved.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.glencoesoftware.omero.ms.image.region;

import java.awt.Color;

import org.testng.Assert;
import org.testng.annotations.Test;

public class RenderingUtilsTest {

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testFlipNullImage() {
        int[] nullArray = null;
        RenderingUtils.flip(nullArray, 4, 4, true, true);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testFlipZeroXImage() {
        int[] src = {1};
        RenderingUtils.flip(src, 0, 4, true, true);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testFlipZeroYImage() {
        int[] src = {1};
        RenderingUtils.flip(src, 4, 0, true, true);
    }

    private void testFlip(
            int[] src, int sizeX, int sizeY,
            boolean flipHorizontal, boolean flipVertical) {
        int[] flipped = RenderingUtils.flip(
                src, sizeX, sizeY, flipHorizontal, flipVertical);
        for (int n = 0; n < sizeX * sizeY; n++){
            int new_col;
            if (flipHorizontal) {
                int old_col = n % sizeX;
                new_col = sizeX - 1 - old_col;
            } else {
                new_col = n % sizeX;
            }
            int new_row;
            if (flipVertical) {
                int old_row = n / sizeX;
                new_row = sizeY - 1 - old_row;
            } else {
                new_row = n / sizeX;
            }
            Assert.assertEquals(flipped[new_row * sizeX + new_col], n);
        }
    }

    private void testAllFlips(int[] src, int sizeX, int sizeY) {
        boolean flipHorizontal = false;
        boolean flipVertical = true;
        testFlip(src, sizeX, sizeY, flipHorizontal, flipVertical);

        flipHorizontal = true;
        flipVertical = false;
        testFlip(src, sizeX, sizeY, flipHorizontal, flipVertical);

        flipHorizontal = true;
        flipVertical = true;
        testFlip(src, sizeX, sizeY, flipHorizontal, flipVertical);
    }

    @Test
    public void testFlipEvenSquare2() {
        int sizeX = 4;
        int sizeY = 4;
        int[] src = new int[sizeX * sizeY];
        for (int n = 0; n < sizeX * sizeY; n++){
            src[n] = n;
        }
        testAllFlips(src, sizeX, sizeY);
    }

    @Test
    public void testFlipOddSquare(){
        int sizeX = 5;
        int sizeY = 5;
        int[] src = new int[sizeX * sizeY];
        for (int n = 0; n < sizeX * sizeY; n++){
            src[n] = n;
        }
        testAllFlips(src, sizeX, sizeY);
    }

    @Test
    public void testFlipWideRectangle() {
        int sizeX = 7;
        int sizeY = 4;
        int[] src = new int[sizeX * sizeY];
        for (int n = 0; n < sizeX * sizeY; n++){
            src[n] = n;
        }
        testAllFlips(src, sizeX, sizeY);
    }

    @Test
    public void testFlipTallRectangle() {
        int sizeX = 4;
        int sizeY = 7;
        int[] src = new int[sizeX * sizeY];
        for (int n = 0; n < sizeX * sizeY; n++){
            src[n] = n;
        }
        testAllFlips(src, sizeX, sizeY);
    }

    @Test
    public void testFlipSingleWidthRectangle() {
        int sizeX = 7;
        int sizeY = 1;
        int[] src = new int[sizeX * sizeY];
        for (int n = 0; n < sizeX * sizeY; n++){
            src[n] = n;
        }
        testAllFlips(src, sizeX, sizeY);
    }

    @Test
    public void testFlipSingleHeightRectangle() {
        int sizeX = 1;
        int sizeY = 7;
        int[] src = new int[sizeX * sizeY];
        for (int n = 0; n < sizeX * sizeY; n++){
            src[n] = n;
        }
        testAllFlips(src, sizeX, sizeY);
    }

    @Test
    public void testFlipSingleEntry() {
        int sizeX = 1;
        int sizeY = 1;
        int[] src = new int[sizeX * sizeY];
        for (int n = 0; n < sizeX * sizeY; n++){
            src[n] = n;
        }
        testAllFlips(src, sizeX, sizeY);
    }

    @Test
    public void testRedOpaque() {
        Color color = new Color(Integer.parseUnsignedInt("FF0000FF", 16), true);
        Assert.assertEquals(color.getRed(), 255);
        Assert.assertEquals(color.getGreen(), 0);
        Assert.assertEquals(color.getBlue(), 0);
        Assert.assertEquals(color.getAlpha(), 255);
    }

    @Test
    public void testRedTransparent() {
        int[] rgba = RenderingUtils.splitHTMLColor("FF000000");
        Assert.assertEquals(255, rgba[0]);
        Assert.assertEquals(0, rgba[1]);
        Assert.assertEquals(0, rgba[2]);
        Assert.assertEquals(0, rgba[3]);
    }

    @Test
    public void testGreenOpaque() {
        int[] rgba = RenderingUtils.splitHTMLColor("00FF00FF");
        Assert.assertEquals(0, rgba[0]);
        Assert.assertEquals(255, rgba[1]);
        Assert.assertEquals(0, rgba[2]);
        Assert.assertEquals(255, rgba[3]);
    }

    @Test
    public void testBlueOpaque() {
        int[] rgba = RenderingUtils.splitHTMLColor("0000FFFF");
        Assert.assertEquals(0, rgba[0]);
        Assert.assertEquals(0, rgba[1]);
        Assert.assertEquals(255, rgba[2]);
        Assert.assertEquals(255, rgba[3]);
    }
}
