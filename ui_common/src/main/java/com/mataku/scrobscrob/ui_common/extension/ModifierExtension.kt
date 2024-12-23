package com.mataku.scrobscrob.ui_common.extension

import androidx.compose.foundation.Indication
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.PointerInputModifierNode
import androidx.compose.ui.unit.IntSize
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ThrottleClickableNode(
  var throttleTime: Long,
  var onClick: () -> Unit,
  var interactionSource: MutableInteractionSource?,
) : PointerInputModifierNode, Modifier.Node() {
  private var invokable = true
  private var lastPress: PressInteraction.Press? = null

  override fun onPointerEvent(pointerEvent: PointerEvent, pass: PointerEventPass, bounds: IntSize) {
    if (invokable) {
      when (pointerEvent.type) {
        PointerEventType.Press -> {
          if (pass == PointerEventPass.Initial) {
            val press = PressInteraction.Press(pointerEvent.changes.last().position)
            interactionSource?.tryEmit(press)
            lastPress = press
          }
        }

        PointerEventType.Release -> {
          if (pass == PointerEventPass.Final) {
            val lastChange = pointerEvent.changes.lastOrNull() ?: return
            if (invokable && lastPress != null) {
              invokable = false
              coroutineScope.launch {
                delay(throttleTime)
                invokable = true
              }
              if (positionWithinBounds(lastChange.position, bounds)) {
                onClick.invoke()
              }
              interactionSource?.tryEmit(
                PressInteraction.Release(lastPress!!)
              )
              lastPress = null
            }
          }
        }
      }
    }
  }

  override fun onCancelPointerInput() {
    lastPress = null
    invokable = true
  }
}

private fun positionWithinBounds(
  position: Offset,
  bounds: IntSize
): Boolean {
  return position.x >= 0 && position.x <= bounds.width &&
    position.y >= 0 && position.y <= bounds.height
}

private data class ThrottleClickableElement(
  val throttleTime: Long,
  val onClick: () -> Unit,
  val interactionSource: MutableInteractionSource?,
) : ModifierNodeElement<ThrottleClickableNode>() {
  override fun create(): ThrottleClickableNode {
    return ThrottleClickableNode(throttleTime, onClick, interactionSource)
  }

  override fun update(node: ThrottleClickableNode) {
    node.throttleTime = throttleTime
    node.onClick = onClick
    node.interactionSource = interactionSource
  }
}

@Composable
fun throttleFirst(
  throttleTimeMs: Long = 500L,
  block: () -> Unit,
): () -> Unit {
  val coroutineScope = rememberCoroutineScope()
  var invokable by remember { mutableStateOf(true) }

  return {
    if (invokable) {
      invokable = false
      coroutineScope.launch {
        delay(throttleTimeMs)
        invokable = true
      }
      block.invoke()
    }
  }
}

fun Modifier.throttleClickable(
  throttleTimeMs: Long = 500L,
  onClick: () -> Unit,
  interactionSource: MutableInteractionSource? = null,
  indication: Indication? = null
): Modifier {
  return this
    .then(
      if (interactionSource != null && indication != null) {
        Modifier
          .indication(
            interactionSource = interactionSource,
            indication = indication
          )
      } else {
        Modifier
      }
    )
    .then(ThrottleClickableElement(throttleTimeMs, onClick, interactionSource))
}
