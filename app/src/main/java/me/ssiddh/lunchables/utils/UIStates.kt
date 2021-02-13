package me.ssiddh.lunchables.utils
import me.ssiddh.lunchables.R

sealed class UIStates {
    abstract val stringResId: Int
    abstract val iconResId: Int

    object ListUIState : UIStates() {
        override val stringResId: Int = R.string.map
        override val iconResId: Int = R.drawable.ic_map
    }
    object MapUIState : UIStates() {
        override val stringResId: Int = R.string.list
        override val iconResId: Int = R.drawable.ic_list
    }
    object NoLocationUIState : UIStates() {
        override val stringResId: Int = R.string.list
        override val iconResId: Int = R.drawable.ic_list
    }
    object ErrorLoadingUIState : UIStates() {
        override val stringResId: Int = R.string.list
        override val iconResId: Int = R.drawable.ic_list
    }
}
