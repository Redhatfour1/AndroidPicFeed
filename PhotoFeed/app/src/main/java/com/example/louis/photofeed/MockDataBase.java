package com.example.louis.photofeed;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by louis on 10/9/17.
 */

public class MockDataBase {
    public static final String image1 = "https://cdn.pixabay.com/photo/2017/03/05/15/29/aston-2118857_1280.jpg";
    public static final String image2 = "https://cdn.pixabay.com/photo/2017/03/15/21/16/checkmated-2147538_1280.jpg";
    public static final String image3 = "https://cdn.pixabay.com/photo/2016/01/29/01/05/magic-cube-1167224_1280.jpg";
    public static final String image4 = "https://cdn.pixabay.com/photo/2014/02/03/16/52/bilardkugeln-257491_1280.png";
    public static final String image5 = "https://cdn.pixabay.com/photo/2016/07/30/16/17/skull-1557446_1280.jpg";

    public static final List<String> allImages = new ArrayList<>();
    static {
        allImages.add(image1);
        allImages.add(image2);
        allImages.add(image3);
        allImages.add(image4);
        allImages.add(image5);
    }
}
