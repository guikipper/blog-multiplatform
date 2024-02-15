package com.example.blogmultiplatform.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.blogmultiplatform.models.ControlStyle
import com.example.blogmultiplatform.models.EditorControl
import com.example.blogmultiplatform.navigation.Screen
import com.varabyte.kobweb.core.rememberPageContext
import kotlinx.browser.document
import kotlinx.browser.localStorage
import org.w3c.dom.HTMLTextAreaElement
import org.w3c.dom.get
import org.w3c.dom.set
import kotlin.js.Date

@Composable
fun isUserLoggedIn (content: @Composable () -> Unit) {
    val context = rememberPageContext()
    val remembered = remember { localStorage["remember"].toBoolean() }
    val userId = remember { localStorage["userId"] }
    var userIdExists by remember { mutableStateOf(false) }

    println("userID: "+userId)
    println("remembered: "+remembered)
    println("userIdExists: "+userIdExists)

    LaunchedEffect(key1 = Unit) {
        userIdExists = if(!userId.isNullOrEmpty()) {
            checkUserId(id = userId)
        } else {
            false
        }
        if (!remembered || !userIdExists) {
            context.router.navigateTo(Screen.AdminLogin.route)
        }
    }

    if (remembered && userIdExists) {
        content()
    } else {
        println("Loading...")
    }
}

fun logout() {
    localStorage["remember"] = "false"
    localStorage["userId"] = ""
    localStorage["username"] = ""
}

fun getEditor() = document.getElementById(Id.editor) as HTMLTextAreaElement

fun getSelectedIntRange(): IntRange? {
    val editor = getEditor()
    val start = editor.selectionStart
    val end = editor.selectionEnd
    return if(start != null && end != null) {
        IntRange(start, (end-1))
    } else {
        return null
    }
}

fun getSelectedText(): String? {
    val range = getSelectedIntRange() //retorna o range
    return if(range != null) {
        var teste = getEditor().value.substring(range)
        println("O teste do substring: "+teste)
        getEditor().value.substring(range) //pegando o valor do editor e dando um substring com o range definido acima.
    } else {
        return null
    }
}

fun applyStyle(controlStyle: ControlStyle) { //recebe um parâmetro do tipo ControlStyle
   val selectedText = getSelectedText() //retorna o texto selecionado
   // println("SELECTED TEXT "+selectedText)
    val selectedIntRange = getSelectedIntRange() //retorna o range novamente
    if (selectedIntRange != null && selectedText != null) {
        getEditor().value = getEditor().value.replaceRange( //substitiu o valor editor por um editado
            range = selectedIntRange,
            replacement = controlStyle.style
        )
        document.getElementById(Id.editorPreview)?.innerHTML = getEditor().value //substitiu o valor do preview pelo getEditor().value
    }
}

fun applyControlStyle(editorControl: EditorControl, onLinkClick: () -> Unit, onImageClick: () -> Unit) {
    when(editorControl) {
        EditorControl.Bold -> {
            applyStyle(
                ControlStyle.Bold(
                    selectedText = getSelectedText()
                )
            )
        }
        EditorControl.Italic -> {
            applyStyle(
                ControlStyle.Italic(
                    selectedText = getSelectedText()
                )
            )
        }
        EditorControl.Link -> {
            onLinkClick()
        }
        EditorControl.Title -> {
            applyStyle(
                ControlStyle.Title(
                    selectedText = getSelectedText()
                )
            )
        }
        EditorControl.Subtitle -> {
            applyStyle(
                ControlStyle.Subtitle(
                    selectedText = getSelectedText()
                )
            )
        }
        EditorControl.Quote -> {
            applyStyle(
                ControlStyle.Quote(
                    selectedText = getSelectedText()
                )
            )
        }
        EditorControl.Code -> {
            applyStyle(
                ControlStyle.Code(
                    selectedText = getSelectedText()
                )
            )
        }
        EditorControl.Image -> {
            onImageClick()
        }
    }
}

fun Long.parseDateString() = Date(this).toLocaleDateString()
//revisar
