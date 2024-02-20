package com.example.blogmultiplatform.pages.admin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.example.blogmultiplatform.components.AdminPanelLayout
import com.example.blogmultiplatform.components.LinkAndImagePopup
import com.example.blogmultiplatform.components.MessagePopup
import com.example.blogmultiplatform.models.Category
import com.example.blogmultiplatform.models.ControlStyle
import com.example.blogmultiplatform.models.EditorControl
import com.example.blogmultiplatform.models.Post
import com.example.blogmultiplatform.models.Theme
import com.example.blogmultiplatform.navigation.Screen
import com.example.blogmultiplatform.styles.EditorKeyStyle
import com.example.blogmultiplatform.util.Constants.FONT_FAMILY
import com.example.blogmultiplatform.util.Constants.SIDE_PANEL_WIDTH
import com.example.blogmultiplatform.util.Id
import com.example.blogmultiplatform.util.applyControlStyle
import com.example.blogmultiplatform.util.applyStyle
import com.example.blogmultiplatform.util.createPost
import com.example.blogmultiplatform.util.getEditor
import com.example.blogmultiplatform.util.getSelectedText
import com.example.blogmultiplatform.util.isUserLoggedIn
import com.example.blogmultiplatform.util.noBorder
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.Resize
import com.varabyte.kobweb.compose.css.ScrollBehavior
import com.varabyte.kobweb.compose.css.Visibility
import com.varabyte.kobweb.compose.file.loadDataUrlFromDisk
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.classNames
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.disabled
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxHeight
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.id
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxHeight
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.onKeyDown
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.resize
import com.varabyte.kobweb.compose.ui.modifiers.scrollBehavior
import com.varabyte.kobweb.compose.ui.modifiers.visibility
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.forms.Switch
import com.varabyte.kobweb.silk.components.forms.SwitchSize
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.layout.SimpleGrid
import com.varabyte.kobweb.silk.components.layout.numColumns
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import kotlinx.browser.document
import kotlinx.browser.localStorage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Input
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.TextArea
import org.jetbrains.compose.web.dom.Ul
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLTextAreaElement
import kotlin.js.Date


data class CreatePageUiState(
    var id: String = "",
    var title: String = "",
    var subtitle: String = "",
    var thumbnail: String = "",
    var thumbnailInputDisabled: Boolean = true,
    var content: String = "",
    var category: Category = Category.Programming,
    var main: Boolean = false,
    var popular: Boolean = false,
    var sponsored: Boolean = false,
    var editorVisibility: Boolean = true,
    var messagePopup: Boolean = false,
    var linkPopup: Boolean = false,
    var imagePopup: Boolean = false
    )
@Page
@Composable
fun Create() {
    isUserLoggedIn {
        CreateScreen()
    }
}

@Composable
fun CreateScreen() {
    val context = rememberPageContext()
    val breakpoint = rememberBreakpoint()
    val scope = rememberCoroutineScope()
    var uiState by remember { mutableStateOf(CreatePageUiState()) }

    AdminPanelLayout{
         Box(
             modifier = Modifier
                 .fillMaxSize()
                 .margin(topBottom = 50.px)
                 .padding(left = if(breakpoint > Breakpoint.MD) SIDE_PANEL_WIDTH.px else 0.px),
             contentAlignment = Alignment.TopCenter
         ) {
             Column(
                 modifier = Modifier
                     .fillMaxSize()
                     .maxWidth(700.px),
                 verticalArrangement = Arrangement.Top,
                 horizontalAlignment = Alignment.CenterHorizontally
             ) {
                 SimpleGrid(numColumns = numColumns(base = 1, sm = 3)) {
                    Row(
                        modifier = Modifier
                        .margin(
                            right = if (breakpoint < Breakpoint.SM) 0.px else 24.px,
                            bottom = if (breakpoint < Breakpoint.SM) 12.px else 0.px
                        ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Switch(
                            modifier = Modifier
                            .margin(right = 8.px),
                            checked = uiState.popular,
                            onCheckedChange = { uiState = uiState.copy(popular = it) },
                            size = SwitchSize.LG
                        )
                        SpanText(
                            modifier = Modifier
                                .fontSize(14.px)
                                //.fontFamily(FONT_FAMILY)
                                .color(Theme.HalfBlack.rgb),
                            text = "Popular"
                        )
                    }
                     //
                     Row(
                         modifier = Modifier
                             .margin(
                                 right = if (breakpoint < Breakpoint.SM) 0.px else 24.px,
                                 bottom = if (breakpoint < Breakpoint.SM) 12.px else 0.px
                             ),
                         verticalAlignment = Alignment.CenterVertically
                     ) {
                         Switch(
                             modifier = Modifier
                                 .margin(right = 8.px),
                             checked = uiState.main,
                             onCheckedChange = { uiState = uiState.copy(main = it) },
                             size = SwitchSize.LG
                         )
                         SpanText(
                             modifier = Modifier
                                 .fontSize(14.px)
                                 //.fontFamily(FONT_FAMILY)
                                 .color(Theme.HalfBlack.rgb),
                             text = "Main"
                         )
                     }
                     //
                     Row(
                         verticalAlignment = Alignment.CenterVertically
                     ) {
                         Switch(
                             modifier = Modifier
                                 .margin(right = 8.px),
                             checked = uiState.sponsored,
                             onCheckedChange = { uiState = uiState.copy(sponsored = it) },
                             size = SwitchSize.LG
                         )
                         SpanText(
                             modifier = Modifier
                                 .fontSize(14.px)
                                 //.fontFamily(FONT_FAMILY)
                                 .color(Theme.HalfBlack.rgb),
                             text = "Sponsored"
                         )
                     }
                 }

                Input(
                    type = InputType.Text,
                    attrs = Modifier
                        .id(Id.titleInput)
                        .fillMaxWidth()
                        .height(54.px)
                        .padding(leftRight = 20.px)
                        .margin(topBottom = 12.px)
                        .backgroundColor(Theme.LightGray.rgb)
                        .borderRadius(r = 4.px)
                        //.noBorder()
                        .noBorder()
                        .fontFamily(FONT_FAMILY)
                        .fontSize(16.px)
                        .toAttrs {
                            attr("placeholder", "Title")
                            //attr("value", uiState.subtitle)
                        }
                )
                 Input(
                     type = InputType.Text,
                     attrs = Modifier
                         .id(Id.subtitleInput)
                         .fillMaxWidth()
                         .height(54.px)
                         .padding(leftRight = 20.px)
                         .margin(bottom = 12.px)
                         .backgroundColor(Theme.LightGray.rgb)
                         .borderRadius(r = 4.px)
                         .noBorder()
                         .fontFamily(FONT_FAMILY)
                         .fontSize(16.px)
                         .toAttrs {
                             attr("placeholder", "Subtitle")
                             //attr("value", uiState.subtitle)
                         }
                 )
                 CategoryDropdown(
                     selectedCategory = uiState.category,
                     onCategorySelect = {
                         uiState = uiState.copy(category = it)
                     }
                 )
                 Row(
                     modifier = Modifier
                         .fillMaxWidth()
                         .margin(bottom = 12.px),
                     verticalAlignment = Alignment.CenterVertically,
                     horizontalArrangement = Arrangement.Start
                 ) {
                     Switch(
                     modifier = Modifier
                         .margin(right = 8.px),
                     checked = !uiState.thumbnailInputDisabled,
                     onCheckedChange = { uiState = uiState.copy(thumbnailInputDisabled = !it ) },
                     size = SwitchSize.LG
                 )
                    SpanText(
                        modifier = Modifier
                            .fontSize(14.px),
                        text = "Paste an Image URL instead"
                    )
                 }
                ThumbnailUploader(
                    thumbnail = uiState.thumbnail,
                    thumbnailInputDisabled = uiState.thumbnailInputDisabled,
                    onThumbnailSelect = {
                        filename, file ->

                        (document.getElementById(Id.thumbnailInput) as HTMLInputElement).value = filename
                        // aqui o input recebe o filename, que é o nome do arquivo
                        //enquanto a variável thumbnail recebe o arquivo em base 64
                        //desas forma é possível alterar novamente o nome do arquivo
                        uiState = uiState.copy(thumbnail = file)

                    }
                )
                 EditorControls(
                     breakpoint = breakpoint,
                     editorVisibility = uiState.editorVisibility,
                     onEditorVisibilityChange = { uiState = uiState.copy(editorVisibility = !uiState.editorVisibility) },
                     onLinkClick = {
                         uiState = uiState.copy(linkPopup = true)
                     },
                     onImageClick = {
                         uiState = uiState.copy(imagePopup = true)
                     }
                 )
                 Editor(editorVisibility = uiState.editorVisibility)
                 CreatePostButton(onClick = {

                     uiState = uiState.copy(title = (document.getElementById(Id.titleInput) as HTMLInputElement).value)
                     uiState = uiState.copy(subtitle = (document.getElementById(Id.subtitleInput) as HTMLInputElement).value)
                     uiState = uiState.copy(content = (document.getElementById(Id.editor) as HTMLTextAreaElement).value)

                     if (!uiState.thumbnailInputDisabled) {
                        uiState = uiState.copy(thumbnail = (document.getElementById(Id.thumbnailInput) as HTMLInputElement).value)
                     }

                     if (
                         uiState.title.isNotEmpty() &&
                         uiState.subtitle.isNotEmpty() &&
                         uiState.thumbnail.isNotEmpty() &&
                         uiState.content.isNotEmpty()
                     ) {
                         scope.launch {
                             val result = createPost(
                                 Post(
                                     author = localStorage.getItem("username").toString(),
                                     title = uiState.title,
                                     subtitle = uiState.subtitle,
                                     date = Date.now().toLong(),
                                     thumbnail = uiState.thumbnail,
                                     content = uiState.content,
                                     category = uiState.category,
                                     popular = uiState.popular,
                                     main = uiState.main,
                                     sponsored = uiState.sponsored
                                 )
                             )
                             if (result) {
                                 context.router.navigateTo(Screen.AdminSuccessPage.route)
                             }
                         }
                     } else {
                         scope.launch {
                             uiState = uiState.copy(messagePopup = true)
                             delay(2000)
                             uiState = uiState.copy(messagePopup = true)
                         }

                     }
                 })
             }
         }
    }
    if (uiState.messagePopup) {
        MessagePopup(
            message = "Please fill out all the fields.",
            onDialogDismiss = {
                uiState = uiState.copy(messagePopup = false)
            }
        )
    }
    if (uiState.linkPopup) {
        LinkAndImagePopup(
            editorControl = EditorControl.Link,
            onAdded = { href, title ->
                applyStyle(
                    ControlStyle.Link(
                        selectedText = getSelectedText(),
                        href = href,
                        title = title
                    )
                )
            },
            onDialogDismiss = { uiState = uiState.copy(linkPopup = false) }
        )
    }

    if (uiState.imagePopup) {
        LinkAndImagePopup(
            editorControl = EditorControl.Image,
            onAdded = { imageUrl, description ->
                applyStyle(
                    ControlStyle.Image(
                        selectedText = getSelectedText(),
                        imageUrl = imageUrl,
                        description = description
                    )
                )
            },
            onDialogDismiss = { uiState = uiState.copy(imagePopup = false) }
        )
    }
}

@Composable
fun ThumbnailUploader(
    thumbnail: String,
    thumbnailInputDisabled: Boolean,
    onThumbnailSelect: (String, String) -> Unit //filename e base 64
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .margin(bottom = 12.px)
            .height(54.px)
    ) {
        Input(
            type = InputType.Text,
            attrs = Modifier
                .id(Id.thumbnailInput)
                .fillMaxSize()
                .margin(right = 12.px)
                .thenIf(
                    condition = thumbnailInputDisabled, //se for true
                    other = Modifier.disabled() //ativar o modifier disabled()
                )
                .padding(leftRight = 20.px)
                .borderRadius(r = 4.px)
                .backgroundColor(Theme.LightGray.rgb)
                .noBorder()
                .toAttrs {
                    attr("placeholder", "Thumbnail")
                    attr("color","red")
                    attr("valur", thumbnail) //pq???????????????
                }
        )
        Button(
            attrs = Modifier
                .onClick {
                    document.loadDataUrlFromDisk(
                        accept = "image/png, imagem/jpeg",
                        onLoad = {
                            onThumbnailSelect(filename, it) //chama a função lambda definida acima
                            //println("Filename: $filename")
                            //println("It: $it")
                        }
                    )
                }
                .fillMaxHeight()
                .padding(leftRight = 24.px)
                .backgroundColor(if(!thumbnailInputDisabled) Theme.Gray.rgb else Theme.Primary.rgb)
                .color(if(!thumbnailInputDisabled) Theme.DarkGray.rgb else Colors.White)
                .noBorder()
                .fontSize(16.px)
                .fontFamily(FONT_FAMILY)
                .borderRadius(r = 4.px)
                .thenIf(
                    condition = !thumbnailInputDisabled, //se for false
                    other = Modifier.disabled() //ativar o modifier disabled()
                    )
                .toAttrs()
        ) {
            SpanText(
                text = "Upload"
            )
        }
    }
}

@Composable
fun EditorControls(
    breakpoint: Breakpoint,
    editorVisibility: Boolean,
    onLinkClick: () -> Unit,
    onImageClick: () -> Unit,
    onEditorVisibilityChange: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        SimpleGrid(
            modifier = Modifier.fillMaxWidth(),
            numColumns = numColumns(base = 1, sm = 2)
        ) {
            Row(
                modifier = Modifier
                    .height(54.px)
                    .fillMaxWidth()
                    .borderRadius(r = 4.px)
                    .backgroundColor(Theme.LightGray.rgb)
            ) {
                EditorControl.values().forEach {
                    EditorControlView(
                        control = it,
                        onClick = {
                            applyControlStyle(
                            it,
                            onLinkClick = onLinkClick,
                            onImageClick = onImageClick
                        ) }
                    )
                }
            }
            Box(
                //modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Button(
                    attrs = Modifier
                        .margin(if (breakpoint < Breakpoint.SM) 12.px else 0.px)
                        .padding(leftRight = 24.px)
                        .thenIf(
                            condition = (breakpoint < Breakpoint.SM),
                            other = Modifier.fillMaxWidth()
                        )
                        .backgroundColor(if (editorVisibility) Theme.LightGray.rgb else Theme.Primary.rgb)
                        .color(if (editorVisibility) Theme.DarkGray.rgb else Colors.White)
                        .height(54.px)
                        .borderRadius(r = 4.px)
                        .noBorder()
                        .onClick {
                            onEditorVisibilityChange()
                            document.getElementById(Id.editorPreview)?.innerHTML = getEditor().value
                            js("hljs.highlightAll()") as Unit
                        }
                        .toAttrs()
                ) {
                    SpanText(
                        modifier = Modifier
                            //.fontFamily(FONT_FAMILY)
                            .fontSize(14.px)
                            .fontWeight(FontWeight.Medium),
                        text = "Preview"
                    )
                }
            }

        }
    }
}

@Composable
fun EditorControlView(control: EditorControl, onClick: () -> Unit) { //vem do enum
    Box(
        modifier = EditorKeyStyle.toModifier()
            .fillMaxHeight()
            .padding(leftRight = 12.px)
            .borderRadius(r = 4.px)
            .cursor(Cursor.Pointer)
            .onClick { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Image(
            src = control.icon,
            description = "${control.name} Icon"
        )
    }
}

@Composable
fun CategoryDropdown(
    selectedCategory: Category,
    onCategorySelect: (Category) -> Unit
) {
    Box(
        modifier = Modifier
            .margin(bottom = 12.px)
            .classNames("dropdown")
            .fillMaxWidth()
            .height(54.px)
            .backgroundColor(Theme.LightGray.rgb)
            .cursor(Cursor.Pointer)
            .attrsModifier {
                attr("data-bs-toggle", "dropdown")
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(leftRight = 20.px),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SpanText(
                modifier = Modifier
                    .fillMaxWidth()
                    .fontFamily(FONT_FAMILY)
                    .fontSize(16.px),
                text = selectedCategory.name
            )
            Box(
                modifier = Modifier
                    .classNames("dropdown-toggle")
            )
        }
        Ul(
            attrs = Modifier
                .fillMaxWidth()
                .classNames("dropdown-menu")
                .toAttrs()
        ) {
            Category.values().forEach { category ->
                Li {
                    A(
                        attrs = Modifier
                            .classNames("dropdown-item")
                            .color(Colors.Black)
                            .fontFamily(FONT_FAMILY)
                            .fontSize(16.px)
                            .onClick { onCategorySelect(category) }
                            .toAttrs()
                    ) {
                        Text(
                            value = category.name
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Editor(editorVisibility: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        TextArea(
            attrs = Modifier
                .id(Id.editor)
                .fillMaxWidth()
                .height(400.px)
                .resize(Resize.None)
                .maxHeight(400.px)
                .margin(top = 12.px)
                .padding(all = 20.px)
                .backgroundColor(Theme.LightGray.rgb)
                .borderRadius(r = 4.px)
                .onKeyDown {
                    if (it.code == "Enter" && it.shiftKey) {
                        println("Clicando alt+Enter")
                        applyStyle(
                            controlStyle = ControlStyle.Break(
                                selectedText = getSelectedText()
                            )
                        )
                    }
                }
                .noBorder()
                .visibility(
                    if(editorVisibility) Visibility.Visible else Visibility.Hidden
                )
                .toAttrs{
                    attr("placeholder", "Post Content")
                }
        ) //Text Area
        Div(
            attrs = Modifier
                .id(Id.editorPreview)
                .fillMaxWidth()
                .height(400.px)
                .maxHeight(400.px)
                .margin(top = 12.px)
                .padding(all = 20.px)
                .backgroundColor(Theme.LightGray.rgb)
                .borderRadius(r = 4.px)
                .noBorder()
                .visibility(
                    if (editorVisibility) Visibility.Hidden else Visibility.Visible
                )
                .overflow(Overflow.Auto)
                .scrollBehavior(ScrollBehavior.Smooth)
                .toAttrs()
        ) {  }
    }
}

@Composable
fun CreatePostButton(onClick: ()-> Unit) {
    Button(
        attrs = Modifier
            .onClick { onClick() }
            .fillMaxWidth()
            .height(54.px)
            .backgroundColor(Theme.Primary.rgb)
            .borderRadius(r = 4.px)
            .color(Theme.LightGray.rgb)
            .margin(top = 12.px)
            .fontSize(16.px)
            .noBorder()
            .toAttrs()
    ) {
        SpanText(
            text = "Create Post"
        )
    }
}