package com.example.blogmultiplatform.pages.admin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.blogmultiplatform.components.AdminPanelLayout
import com.example.blogmultiplatform.components.Posts
import com.example.blogmultiplatform.components.SearchBar
import com.example.blogmultiplatform.models.ApiListResponse
import com.example.blogmultiplatform.models.PostWithoutDetails
import com.example.blogmultiplatform.models.Theme
import com.example.blogmultiplatform.util.Constants.FONT_FAMILY
import com.example.blogmultiplatform.util.Constants.SIDE_PANEL_WIDTH
import com.example.blogmultiplatform.util.fetchMyPosts
import com.example.blogmultiplatform.util.isUserLoggedIn
//import com.example.blogmultiplatform.util.noBorder
//import com.example.blogmultiplatform.util.testPosts
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.silk.components.forms.Switch
import com.varabyte.kobweb.silk.components.forms.SwitchSize
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import kotlinx.browser.localStorage
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Button
import org.w3c.dom.get

@Page
@Composable
fun MyPostsPage() {
    isUserLoggedIn {
        MyPostsScreen()
    }
}

@Composable
fun MyPostsScreen() {
    val breakpoint = rememberBreakpoint()
    var selectable by remember { mutableStateOf(false) }
    var spanText by remember { mutableStateOf("Select") }
    val myPosts = remember { mutableStateListOf<PostWithoutDetails>() }
    //val userId = remember { localStorage["userId"] }

    LaunchedEffect(Unit) {
        //testPosts(id = userId)
        fetchMyPosts(
            skip = 0,
            onSuccess = {
                if (it is ApiListResponse.Success) {
                    myPosts.addAll(it.data)
                }
            },
            onError = {
                println("Erro..." + it)
            }
        )
    }


    AdminPanelLayout{
        Column(
            modifier = Modifier
                .margin(topBottom = 50.px)
                .fillMaxSize()
                .padding(left = if (breakpoint > Breakpoint.MD) SIDE_PANEL_WIDTH.px else 0.px),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(if (breakpoint > Breakpoint.MD) 20.percent else 50.percent)
                    .margin(bottom = 24.px),
                contentAlignment = Alignment.Center
            ) {
                SearchBar (
                    onEnterClick = {}
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(
                        if (breakpoint > Breakpoint.MD) 80.percent
                        else 90.percent
                    )
                    .margin(bottom = 24.px),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Switch(
                        modifier = Modifier
                            .margin(right = 8.px),
                        size = SwitchSize.LG,
                        checked = selectable,
                        onCheckedChange = { selectable = it }
                    )
                    SpanText(
                        modifier = Modifier
                            .color(if (selectable) Colors.Black else Theme.HalfBlack.rgb ),
                        text = spanText
                    )
                }
                    Button(
                        attrs = Modifier
                            .height(54.px)
                            .padding(leftRight = 24.px)
                            .backgroundColor(Theme.Red.rgb)
                            .color(Colors.White)
                            .borderRadius(r = 4.px)
                            .fontSize(14.px)
                            //.noBorder()
                            .fontFamily(FONT_FAMILY)
                            .fontWeight(FontWeight.Medium)
                            .onClick {

                            }
                            .toAttrs()
                    ) {
                        SpanText(
                            text = "Delete"
                        )
                    }
                }
            Posts(posts = myPosts)
        }
    }
}
