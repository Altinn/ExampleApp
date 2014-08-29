package com.altinn.apps.fisher.ui.component;
/**
 * This class is used to support Custom font in AutoCompleteTextView
 */
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

import com.altinn.apps.fisher.CacheManager;
import com.altinn.apps.fisher.R;

public class RAutoCompleteTextView extends AutoCompleteTextView {

	
	public RAutoCompleteTextView(Context context) {
		super(context);
	}

	
	public RAutoCompleteTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		parseAttributes(context,attrs);
	}

	public RAutoCompleteTextView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		parseAttributes(context,attrs);
	}
	
	/**
	 * This method loads appropriate font and set it 
	 * @param context
	 * @param attrs
	 */
	private void parseAttributes(Context context, AttributeSet attrs) {
	    TypedArray values = context.obtainStyledAttributes(attrs, R.styleable.RAutoCompleteTextView);

	    int typefaceValue = values.getInt(R.styleable.RAutoCompleteTextView_typeface, 0);
	    values.recycle();

	    setTypeface(CacheManager.getInstance().getTypeFace(typefaceValue));
	}

}
