package com.noxcrew.interfaces.view

import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryCloseEvent
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * Represents a single view of an interface. The interface itself may be seen by multiple players,
 * but this view was created for a specific viewer.
 */
public interface InterfaceView {

    /** The player this view is for. */
    public val player: Player

    /**
     * Tracks whether this menu should be opened or not. This does not actually represent
     * whether the menu is open currently, it represents whether the code wants it to be
     * open. This does not get set to false if the menu is closed by the player, it gets
     * set to false if the code calls close() which is a manual request to make sure this
     * menu doesn't do anything anymore!
     */
    public val shouldStillBeOpened: Boolean

    /** Opens up this view. */
    public suspend fun open()

    /** Closes this view. */
    public suspend fun close(
        reason: InventoryCloseEvent.Reason = InventoryCloseEvent.Reason.PLUGIN,
        closeInventory: Boolean = reason != InventoryCloseEvent.Reason.OPEN_NEW
    )

    /** Returns whether this view is opened based on the player's current shown inventory. */
    public fun isOpen(): Boolean

    /** Returns the parent view, or `null` if this view has no parent. */
    public fun parent(): InterfaceView?

    /** Re-opens the [parent] view, or closes this menu if it has no parent. */
    public suspend fun back()

    /** Sets the title fo this view to [value]. */
    public fun title(value: Component)

    /** Called whenever a view is opened. */
    public fun onOpen()

    /** Redraws all transforms in this view. */
    public fun redrawComplete()

    /**
     * Sends a chat query for this player. This view will temporarily close, allowing the player
     * to send a chat message. After a chat message is sent the result is passed to a predicate
     * and this view is re-opened. While this view is minimised it still respects all normal rules
     * and closing will still work properly. Opening any other inventory will end the query automatically.
     *
     * Will fail if this view is not open.
     */
    public fun runChatQuery(
        timeout: Duration = 30.seconds,
        onCancel: () -> Unit = {},
        onComplete: (Component) -> Unit
    )
}
