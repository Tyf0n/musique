package net.sourceforge.jaad.mp4.boxes.impl.meta;

import net.sourceforge.jaad.mp4.MP4InputStream;
import net.sourceforge.jaad.mp4.boxes.FullContainerBox;

import java.io.IOException;

/**
 * Author: Denis Tulskiy
 * Date: 4/3/11
 */
public class MetaBox extends FullContainerBox {
    public MetaBox() {
        super("Meta Box", "meta");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);
        readChildren(in);
    }
}
