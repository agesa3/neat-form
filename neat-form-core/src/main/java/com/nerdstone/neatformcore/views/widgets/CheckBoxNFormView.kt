package com.nerdstone.neatformcore.views.widgets

import android.content.Context
import android.util.AttributeSet
import android.widget.CheckBox
import com.nerdstone.neatformcore.R
import com.nerdstone.neatformcore.domain.listeners.DataActionListener
import com.nerdstone.neatformcore.domain.listeners.VisibilityChangeListener
import com.nerdstone.neatformcore.domain.model.NFormViewDetails
import com.nerdstone.neatformcore.domain.model.NFormViewProperty
import com.nerdstone.neatformcore.domain.view.FormValidator
import com.nerdstone.neatformcore.domain.view.NFormView
import com.nerdstone.neatformcore.rules.NeatFormValidator
import com.nerdstone.neatformcore.utils.ViewUtils
import com.nerdstone.neatformcore.utils.removeAsterisk
import com.nerdstone.neatformcore.views.builders.CheckBoxViewBuilder
import com.nerdstone.neatformcore.views.handlers.ViewDispatcher
import com.nerdstone.neatformcore.views.handlers.ViewVisibilityChangeHandler

class CheckBoxNFormView : CheckBox, NFormView {
    var attributeSet: AttributeSet? = null
    override lateinit var viewProperties: NFormViewProperty
    override var dataActionListener: DataActionListener? = null
    override var visibilityChangeListener: VisibilityChangeListener? =
        ViewVisibilityChangeHandler.INSTANCE
    override val viewBuilder = CheckBoxViewBuilder(this)
    override val viewDetails = NFormViewDetails(this)
    override var formValidator: FormValidator = NeatFormValidator.INSTANCE

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        attributeSet = attrs
    }

    override fun initView(
        viewProperty: NFormViewProperty, viewDispatcher: ViewDispatcher
    ): NFormView {
        ViewUtils.setupView(this, viewProperty, viewDispatcher)
        setupViewAttributes(attributeSet)
        return this
    }


    override fun setChecked(checked: Boolean) {
        super.setChecked(checked)
        dataActionListener?.also {
            if (checked) {
                this.viewDetails.value =
                    mutableMapOf(viewDetails.name to text.toString().removeAsterisk())
            } else {
                this.viewDetails.value = null
            }
            it.onPassData(viewDetails)
        }
    }

    override fun resetValueWhenHidden() {
        isChecked = false
    }

    override fun trackRequiredField() = ViewUtils.handleRequiredStatus(this)

    override fun validateValue(): Boolean {
        val validationPair = formValidator.validateField(this)
        if (!validationPair.first) {
            this.error = validationPair.second
        } else this.error = null
        return validationPair.first
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        visibilityChangeListener?.onVisibilityChanged(this, visibility)
    }

    /**
     * Obtain custom xml attributes passed for the stepper view
     */
    private fun setupViewAttributes(attrs: AttributeSet?) {
        if (attrs != null) {
            val typedArray = context.theme.obtainStyledAttributes(
                attrs, R.styleable.CheckBoxNFormView, 0, 0
            )
            try {
                viewBuilder.also {
                    val fontSize: Int =
                        typedArray.getDimensionPixelSize(
                            R.styleable.CheckBoxNFormView_checkbox_text_size,
                            12
                        )
                    viewProperties.viewAttributes?.put(
                        CheckBoxViewBuilder.CheckBoxProperties.CHECK_BOX_TEXT_SIZE.name,
                        fontSize
                    )
                    it.buildView()
                }
            } finally {
                typedArray.recycle()
            }
        }
    }
}