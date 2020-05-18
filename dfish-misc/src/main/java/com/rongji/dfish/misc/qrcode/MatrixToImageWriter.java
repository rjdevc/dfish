/*
 * Copyright 2009 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rongji.dfish.misc.qrcode;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.rongji.dfish.base.util.LogUtil;

/**
 * Writes a {@link BitMatrix} to {@link BufferedImage}, file or stream. Provided
 * here instead of core since it depends on Java SE libraries.
 *
 * @author Sean Owen
 */
public final class MatrixToImageWriter {

	private static final MatrixToImageConfig DEFAULT_CONFIG = new MatrixToImageConfig();

	private MatrixToImageWriter() {
	}

	public static BufferedImage toBufferedImage(String content, int size) {
		return toBufferedImage(toBitMatrix(content, size));
	}
	/**
	 * Renders a {@link BitMatrix} as an image, where "false" bits are rendered
	 * as white, and "true" bits are rendered as black. Uses default
	 * configuration.
	 *
	 * @param matrix
	 *            {@link BitMatrix} to write
	 * @return {@link BufferedImage} representation of the input
	 */
	public static BufferedImage toBufferedImage(BitMatrix matrix) {
		return toBufferedImage(matrix, DEFAULT_CONFIG);
	}

	/**
	 * As {@link #toBufferedImage(BitMatrix)}, but allows customization of the
	 * output.
	 *
	 * @param matrix
	 *            {@link BitMatrix} to write
	 * @param config
	 *            output configuration
	 * @return {@link BufferedImage} representation of the input
	 */
	public static BufferedImage toBufferedImage(BitMatrix matrix, MatrixToImageConfig config) {
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		BufferedImage image = new BufferedImage(width, height, config.getBufferedImageColorModel());
		int onColor = config.getOnColor();
		int offColor = config.getOffColor();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, matrix.get(x, y) ? onColor : offColor);
			}
		}
		return image;
	}

	public static BitMatrix toBitMatrix(String content, int size) {
		MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
		Map<EncodeHintType, String> hints = new HashMap<>();
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		BitMatrix matrix = null;
		try {
			matrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, size, size, hints);
		} catch (WriterException e) {
			LogUtil.error("二维码构建异常", e);;
		}
		return matrix;
	}
	
	public static void writeToStream(String content, String format, int size, OutputStream stream) throws IOException {
		writeToStream(toBitMatrix(content, size), format, stream);
	}
	
	/**
	 * Writes a {@link BitMatrix} to a stream with default configuration.
	 *
	 * @param matrix
	 *            {@link BitMatrix} to write
	 * @param format
	 *            image format
	 * @param stream
	 *            {@link OutputStream} to write image to
	 * @throws IOException
	 *             if writes to the stream fail
	 * @see #toBufferedImage(BitMatrix)
	 */
	public static void writeToStream(BitMatrix matrix, String format, OutputStream stream) throws IOException {
		writeToStream(matrix, format, stream, DEFAULT_CONFIG);
	}

	/**
	 * As {@link #writeToStream(BitMatrix, String, OutputStream)}, but allows
	 * customization of the output.
	 *
	 * @param matrix
	 *            {@link BitMatrix} to write
	 * @param format
	 *            image format
	 * @param stream
	 *            {@link OutputStream} to write image to
	 * @param config
	 *            output configuration
	 * @throws IOException
	 *             if writes to the stream fail
	 */
	public static void writeToStream(BitMatrix matrix, String format, OutputStream stream, MatrixToImageConfig config)
			throws IOException {
		BufferedImage image = toBufferedImage(matrix, config);
		if (!ImageIO.write(image, format, stream)) {
			throw new IOException("Could not write an image of format " + format);
		}
	}
	
}
