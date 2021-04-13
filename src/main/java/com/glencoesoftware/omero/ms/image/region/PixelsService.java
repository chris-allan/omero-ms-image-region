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

import java.io.File;

import org.slf4j.LoggerFactory;

import ome.api.IQuery;
import ome.io.nio.BackOff;
import ome.io.nio.FilePathResolver;
import ome.io.nio.PixelBuffer;
import ome.io.nio.TileSizes;
import ome.model.core.Pixels;
/**
 * Subclass which overrides series retrieval to avoid the need for
 * an injected {@link IQuery}.
 * @author Chris Allan <callan@glencoesoftware.com>
 *
 */
public class PixelsService extends ome.io.nio.PixelsService {

    private static final org.slf4j.Logger log =
            LoggerFactory.getLogger(ImageRegionRequestHandler.class);

    public PixelsService(
            String path, long memoizerWait, FilePathResolver resolver,
            BackOff backOff, TileSizes sizes, IQuery iQuery) {
        super(
            path, true, new File(new File(path), "BioFormatsCache"),
            memoizerWait, resolver, backOff, sizes, iQuery);
        log.info("Using image region PixelsService");
    }

    @Override
    protected int getSeries(Pixels pixels) {
        return pixels.getImage().getSeries();
    }

    /**
     * Returns a pixel buffer for a given set of pixels. Either a proprietary
     * ROMIO pixel buffer or a specific pixel buffer implementation.
     * @param pixels Pixels set to retrieve a pixel buffer for.
     * @param write Whether or not to open the pixel buffer as read-write.
     * <code>true</code> opens as read-write, <code>false</code> opens as
     * read-only.
     * @return A pixel buffer instance.
     */
    public PixelBuffer getPixelBuffer(Pixels pixels, boolean write)
    {
        PixelBuffer pb = _getPixelBuffer(pixels, write);
        if (log.isDebugEnabled()) {
            log.debug(pb +" for " + pixels);
        }
        return pb;
    }

    /**
     * Returns a ZarrPixelBuffer for a given set of pixels.
     * @param pixels Pixels set to retrieve a pixel buffer for.
     * @param write Whether or not to open the pixel buffer as read-write.
     * <code>true</code> opens as read-write, <code>false</code> opens as
     * read-only.
     * @return A pixel buffer instance.
     */
    public PixelBuffer getZarrPixelBuffer(
            Pixels pixels, String ngffDir, OmeroZarrUtils zarrUtils) {
        log.info("Creating ZarrPixelBuffer");
        return new ZarrPixelBuffer(
            pixels, ngffDir, pixels.getImage().getFileset().getId(), zarrUtils);
    }

    /**
     * Get a ZarrPixelBuffer if possible, otherwise try to get a TiledbPixelBuffer
     * @param pixels Pixels set to retrieve a pixel buffer for.
     * @param ngffDir Top-level directory containing NGFF files
     * @param tiledbUtils Configured TiledbUtils
     * @param zarrUtils Configured OmeroZarrUtils
     * @return ZarrPixelBuffer or TiledbPixelBuffer
     */
    public PixelBuffer getNgffPixelBuffer(
            Pixels pixels, String ngffDir,
            OmeroZarrUtils zarrUtils) {
            return new ZarrPixelBuffer(
                pixels, ngffDir, pixels.getImage().getFileset().getId(),
                zarrUtils);
    }
}
