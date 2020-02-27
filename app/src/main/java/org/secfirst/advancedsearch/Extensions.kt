package org.secfirst.advancedsearch

import org.jsoup.nodes.TextNode

fun TextNode.wrapTextWithElement(strToWrap: String, wrapperHTML: String) {
    var textNode = this
    while (textNode.text().contains(strToWrap)) {
        val rightNodeFromSplit =
            textNode.splitText(textNode.text().indexOf(strToWrap))
        if (rightNodeFromSplit.text().length > strToWrap.length) {
            textNode = rightNodeFromSplit.splitText(strToWrap.length)
            rightNodeFromSplit.wrap(wrapperHTML)
        } else {
            rightNodeFromSplit.wrap(wrapperHTML)
            return
        }
    }
}