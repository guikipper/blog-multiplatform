package com.example.blogmultiplatform.pages.admin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.example.blogmultiplatform.models.Theme
import com.example.blogmultiplatform.models.User
import com.example.blogmultiplatform.models.UserWithoutPassword
import com.example.blogmultiplatform.navigation.Screen
import com.example.blogmultiplatform.styles.loginInputStyle
import com.example.blogmultiplatform.util.Constants.FONT_FAMILY
import com.example.blogmultiplatform.util.Id
import com.example.blogmultiplatform.util.Res
import com.example.blogmultiplatform.util.checkUserExistence
import com.example.blogmultiplatform.util.noBorder
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.id
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.outline
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.textAlign
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import kotlinx.browser.document
import kotlinx.browser.localStorage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Input
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.set

@Page
@Composable
fun LoginScreen() {
    val scope = rememberCoroutineScope()
    var errorText by remember { mutableStateOf(" ") }
    val context = rememberPageContext()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(leftRight = 50.px, top = 80.px, bottom = 25.px)
                .backgroundColor(Theme.LightGray.rgb),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier
                    .margin(bottom = 50.px)
                    .width(100.px),
                src = Res.Image.logo,
                description = "logo image"
            )
            Input(
                type = InputType.Text,
                attrs = loginInputStyle.toModifier()
                    .id(Id.usernameInput)
                    .margin(12.px)
                    .width(350.px)
                    .height(54.px)
                    .padding(leftRight = 20.px)
                    .outline(
                        width = 0.px,
                        style = LineStyle.None,
                        color = Colors.Transparent
                    )
                    .backgroundColor(Colors.White)
                    .fontFamily(FONT_FAMILY)
                    .fontSize(14.px)
                    .toAttrs{
                        attr("placeholder", "username")
                    }
            )
            Input(
                type = InputType.Password,
                attrs = loginInputStyle.toModifier()
                    .id(Id.passwordInput)
                    .margin(bottom = 20.px)
                    .width(350.px)
                    .height(54.px)
                    .padding(leftRight = 20.px)
                    .outline(
                        width = 0.px,
                        style = LineStyle.None,
                        color = Colors.Transparent
                    )
                    .backgroundColor(Colors.White)
                    .fontFamily(FONT_FAMILY)
                    .fontSize(14.px)
                    .toAttrs{
                        attr("placeholder", "password")
                    }
            )
            Button(
                attrs = Modifier
                    .width(350.px)
                    .height(54.px)
                    .margin(bottom = 24.px)
                    .backgroundColor(Theme.Primary.rgb)
                    .color(Colors.White)
                    .noBorder()
                    .cursor(Cursor.Pointer)
                    .onClick {
                        println("Login")
                        scope.launch {
                            val username = (document.getElementById(Id.usernameInput) as HTMLInputElement).value
                            val password = (document.getElementById(Id.passwordInput) as HTMLInputElement).value
                            if (username.isNotEmpty() && password.isNotEmpty()) {

                                val user = checkUserExistence(
                                    user = User(
                                        username = username,
                                        password = password
                                    )
                                )
                                if (user != null) {
                                    println("Tela de Login, use existe, segue: "+ user)
                                    rememberLoggedIn(remember = true, user = user)
                                    context.router.navigateTo(Screen.AdminHome.route)
                                } else {
                                    errorText = "The user doens't exist."
                                    delay(3000)
                                    errorText = " "
                                }
                            } else {
                                errorText = "Input fields is empty."
                                delay(3000)
                                errorText = " "
                            }
                        }

                    }
                    .borderRadius(r = 4.px)
                    .fontWeight(FontWeight.Medium)
                    //.fontFamily(FONT_FAMILY)
                    .fontSize(14.px)
                    .toAttrs()
            ) {
                SpanText(
                    text = "Sign In"
                )
            }
            SpanText(modifier = Modifier
                .width(350.px)
                .color(Colors.Red)
                .fontFamily(FONT_FAMILY)
                .textAlign(TextAlign.Center),
                text = errorText
            )
        }
    }
}

private fun rememberLoggedIn(remember: Boolean, user: UserWithoutPassword? = null) {
    localStorage["remember"] = remember.toString()
    if (user != null) {
        localStorage["userId"] = user.id
        localStorage["username"] = user.username
    }
}