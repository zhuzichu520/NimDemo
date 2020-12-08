package com.chuzi.android.shared.rxhttp.parser

import com.chuzi.android.shared.http.entity.PageList
import okhttp3.Response
import rxhttp.wrapper.annotation.Parser
import rxhttp.wrapper.parse.AbstractParser
import rxhttp.wrapper.utils.convert
import java.lang.reflect.Type

@Parser(
    name = "Response",
    wrappers = [List::class, PageList::class]
)
open class ResponseParser<T> : AbstractParser<T> {
    //注意，以下两个构造方法是必须的
    protected constructor() : super() {}
    constructor(type: Type?) : super(type!!) {}

    override fun onParse(response: Response): T {
        return response.convert(mType)
    }

}