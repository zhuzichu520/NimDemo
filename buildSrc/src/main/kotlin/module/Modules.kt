package module

/**
 * desc
 * author: 朱子楚
 * time: 2020/9/10 1:46 PM
 * since: v 1.0.0
 */
data class Modules(
    var data: ArrayList<Module> = arrayListOf()

) {
    override fun toString(): String {
        return "Modules(data=$data)"
    }
}