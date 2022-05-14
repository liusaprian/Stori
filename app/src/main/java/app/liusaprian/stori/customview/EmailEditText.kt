package app.liusaprian.stori.customview

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.util.PatternsCompat
import app.liusaprian.stori.R

class EmailEditText : AppCompatEditText {

    constructor(context: Context) : super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                error = if (!isValidEmail(s)) resources.getString(R.string.incorrect_email_format) else null
            }
            override fun afterTextChanged(s: Editable) {
                // Do nothing.
            }
        })

    }

    private fun isValidEmail(email: CharSequence): Boolean {
        return PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }
}