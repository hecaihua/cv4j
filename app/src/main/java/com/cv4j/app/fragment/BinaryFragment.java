package com.cv4j.app.fragment;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cv4j.app.R;
import com.cv4j.app.app.BaseFragment;
import com.cv4j.core.binary.ConnectedAreaLabel;
import com.cv4j.core.binary.MorphOpen;
import com.cv4j.core.binary.Threshold;
import com.cv4j.core.datamodel.ByteProcessor;
import com.cv4j.core.datamodel.CV4JImage;
import com.cv4j.core.datamodel.Rect;
import com.cv4j.core.datamodel.Size;
import com.cv4j.image.util.Tools;
import com.safframework.injectview.Injector;
import com.safframework.injectview.annotations.InjectView;
import com.safframwork.tony.common.utils.Preconditions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tony Shen on 2017/4/16.
 */

public class BinaryFragment extends BaseFragment {

    @InjectView(R.id.image0)
    ImageView image0;

    @InjectView(R.id.image1)
    ImageView image1;

    @InjectView(R.id.image2)
    ImageView image2;

    @InjectView(R.id.image3)
    ImageView image3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_binary, container, false);
        Injector.injectInto(this, v);

        initData();

        return v;
    }

    private void initData() {

        Resources res = getResources();
        final Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.test_binary1);
        image0.setImageBitmap(bitmap);

        CV4JImage cv4JImage = new CV4JImage(bitmap);
        Threshold threshold = new Threshold();
        threshold.process((ByteProcessor)(cv4JImage.convert2Gray().getProcessor()),Threshold.THRESH_TRIANGLE,Threshold.METHOD_THRESH_BINARY_INV,255);
        image1.setImageBitmap(cv4JImage.getProcessor().getImage().toBitmap());

        MorphOpen morphOpen = new MorphOpen();
        cv4JImage.resetBitmap();
        morphOpen.process((ByteProcessor)cv4JImage.getProcessor(),new Size(5));

        image2.setImageBitmap(cv4JImage.getProcessor().getImage().toBitmap());

        ConnectedAreaLabel connectedAreaLabel = new ConnectedAreaLabel();
        byte[] mask = new byte[cv4JImage.getProcessor().getWidth()*cv4JImage.getProcessor().getHeight()];
        List<Rect> rectangles = new ArrayList<>();
        connectedAreaLabel.process((ByteProcessor)cv4JImage.getProcessor(),mask,rectangles,true);
        cv4JImage.resetBitmap();
        Bitmap newBitmap = cv4JImage.getProcessor().getImage().toBitmap();

        if (Preconditions.isNotBlank(rectangles)) {
            Tools.drawRects(newBitmap,rectangles);
        }

        image3.setImageBitmap(newBitmap);
    }
}