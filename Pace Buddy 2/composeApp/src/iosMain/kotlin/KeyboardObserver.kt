import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreGraphics.CGFloat
import platform.Foundation.*
import platform.UIKit.*
import platform.darwin.NSObject

@OptIn(ExperimentalForeignApi::class)
fun observeKeyboardHeight(onChange: (CGFloat) -> Unit) {
    NSNotificationCenter.defaultCenter.addObserver(
        observer = object : NSObject() {},
        selector = NSSelectorFromString("keyboardWillChangeFrame:"),
        name = UIKeyboardWillChangeFrameNotification,
        `object` = null
    )
    // In your selector method:
    // val userInfo = notification.userInfo
    // val frame = userInfo?.get(UIKeyboardFrameEndUserInfoKey) as CGRect
    // onChange(frame.height)
}
