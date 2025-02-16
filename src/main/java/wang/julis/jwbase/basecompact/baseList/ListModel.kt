package wang.julis.jwbase.basecompact.baseList

/**
 *
 * Created by @juliswang on 2025/02/14 16:40
 *
 * @Description
 */
data class ListModel(
    var activityName: String? = null,
    var activityClass: Class<*>? = null,
    var onClick: (() -> Unit)? = null
) {
    constructor(activityName: String, activityClass: Class<*>) : this(activityName, activityClass, null)
}