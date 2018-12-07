package xcvf.top.readercore.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.List;

import xcvf.top.readercore.bean.Line;
import xcvf.top.readercore.bean.Page;
import xcvf.top.readercore.bean.TextConfig;
import xcvf.top.readercore.bean.TxtChar;

/**
 * Created by bifan-wei
 * on 2017/11/27.
 */

public class TxtBitmapUtil {

    private static final boolean DEBUG = false;

    public static final Bitmap createHorizontalPage(Context context,Bitmap bg, TextConfig config, Page page) {

        Bitmap bitmap = bg.copy(Bitmap.Config.RGB_565, true);
        Canvas canvas = new Canvas(bitmap);
        List<Line> lines = page.getLines();
        if (lines == null || lines.size() == 0) {
            return null;
        }
        int line = page.getLines().size();
        Paint paint = config.getSamplePaint();
        paint.setColor(context.getResources().getColor(config.getTextColor()));
        int lineHeight = config.getTextSize();
        int sx = 0, sy = lineHeight + config.paddingTop;
        for (int i = 0; i < line; i++) {
            Line line1 = (Line) page.getLines().get(i);
            int charLength = line1.getChars().size();
            for (int j = 0; j < charLength; j++) {
                //一个字符
                TxtChar txtChar = line1.getChars().get(j);
                String str = String.valueOf(txtChar.getData());
                canvas.drawText(str, sx, sy, paint);
                sx += txtChar.getWidth();
            }
            if (DEBUG) {
                canvas.drawLine(0, sy, 1000, sy, paint);
                canvas.drawText(String.valueOf(i), 0, sy, paint);
            }
            sx = 0;
            sy += (lineHeight + config.getLineSpace());
        }

        return bitmap;
    }

    public static Bitmap CreateBitmap(int bitmapStyleColor, int bitmapWidth, int bitmapHeight) {
        int[] BitmapColor = getBitmapColor(bitmapStyleColor, bitmapWidth, bitmapHeight);
        return Bitmap.createBitmap(BitmapColor, bitmapWidth, bitmapHeight, Bitmap.Config.RGB_565);
    }

    public static Bitmap CreateBitmap(Resources res, int backgroundResource, int bitmapWidth, int bitmapHeight) {
        Bitmap bgBitmap = BitmapFactory.decodeResource(res, backgroundResource);
        int width = bgBitmap.getWidth();
        int height = bgBitmap.getHeight();
        int[] color = new int[width * height];
        for (int y = 0; y < height; y++) {// use of x,y is legible then // the
            for (int x = 0; x < width; x++) {
                color[y * width + x] = bgBitmap.getPixel(x, y);// the shift
            }
        }
        int[] colors = new int[bitmapWidth * bitmapHeight];
        for (int y = 0, size = bitmapWidth * bitmapHeight, border = width * height, index = 0; y < size; y++) {
            if (index == border) {
                index = 0;
            }
            colors[y] = color[index];
            index++;
        }
        return Bitmap.createBitmap(colors, bitmapWidth, bitmapHeight, Bitmap.Config.RGB_565);
    }

    private static int[] getBitmapColor(int color, int with, int height) {
        int[] colors = new int[with * height];
        int STRIDE = height;
        int c = color;
        for (int y = 0; y < with; y++) {// use of x,y is legible then // the //
            for (int x = 0; x < height; x++) {
                colors[y * STRIDE + x] = c;// the shift operation generates
            }
        }
        return colors;
    }


    public int[] getImagePixel(Resources res, int drawable) {
        Bitmap bi = BitmapFactory.decodeResource(res, drawable);
        int with = bi.getWidth();
        int height = bi.getHeight();
        int[] colors = new int[with * height];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < with; j++) {
                int pixel = bi.getPixel(i, j); // 下面三行代码将一个数字转换为RGB数字
                colors[i * with + j] = pixel;
            }
        }
        return colors;
    }
}
