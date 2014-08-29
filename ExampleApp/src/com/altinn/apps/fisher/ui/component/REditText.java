package com.altinn.apps.fisher.ui.component;
/**
 * This class is used to support Custom font in EditText
 */
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.EditText;

import com.altinn.apps.fisher.CacheManager;
import com.altinn.apps.fisher.R;

public class REditText extends EditText {

	public REditText(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public REditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		parseAttributes(context,attrs);
	}

	public REditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		parseAttributes(context,attrs);
	}
	
	/**
	 * This method loads appropriate font and set it 
	 * @param context
	 * @param attrs
	 */
	private void parseAttributes(Context context, AttributeSet attrs) {
	    TypedArray values = context.obtainStyledAttributes(attrs, R.styleable.REditText);

	    int typefaceValue = values.getInt(R.styleable.REditText_typeface, 0);
	    values.recycle();

	    setTypeface(CacheManager.getInstance().getTypeFace(typefaceValue));
	}

}
