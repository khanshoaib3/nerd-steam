package com.github.khanshoaib3.steamcompanion.ui.utils

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import java.util.EnumSet

enum class Side {
    Start, Top, End, Bottom,
}

class SideSet private constructor(
    private val sides: EnumSet<Side>
) {
    constructor(vararg sides: Side) : this(EnumSet.noneOf(Side::class.java)) {
        this.sides.addAll(sides)
    }

    operator fun plus(side: Side): SideSet {
        val newSet = EnumSet.copyOf(sides)
        newSet.add(side)
        return SideSet(newSet)
    }

    operator fun plus(other: SideSet): SideSet {
        val newSet = EnumSet.copyOf(sides)
        newSet.addAll(other.sides)
        return SideSet(newSet)
    }

    operator fun minus(side: Side): SideSet {
        val newSet = EnumSet.copyOf(sides)
        newSet.remove(side)
        return SideSet(newSet)
    }

    operator fun minus(other: SideSet): SideSet {
        val newSet = EnumSet.copyOf(sides)
        newSet.removeAll(other.sides)
        return SideSet(newSet)
    }

    operator fun contains(side: Side): Boolean = sides.contains(side)

    override fun toString(): String = sides.joinToString(", ")
}

operator fun Side.plus(other: Side): SideSet = SideSet(this, other)
operator fun Side.plus(other: SideSet): SideSet = SideSet(this) + other

fun PaddingValues.removePaddings(
    sides: SideSet,
    layoutDirection: LayoutDirection = LayoutDirection.Ltr
): PaddingValues {
    val start =
        if (sides.contains(Side.Start)) 0.dp else this.calculateStartPadding(layoutDirection)
    val top = if (sides.contains(Side.Top)) 0.dp else this.calculateTopPadding()
    val end = if (sides.contains(Side.End)) 0.dp else this.calculateEndPadding(layoutDirection)
    val bottom = if (sides.contains(Side.Bottom)) 0.dp else this.calculateBottomPadding()
    return PaddingValues(start = start, top = top, end = end, bottom = bottom)
}

fun PaddingValues.removePaddings(
    side: Side,
    layoutDirection: LayoutDirection = LayoutDirection.Ltr
): PaddingValues {
    val start = if (side == Side.Start) 0.dp else this.calculateStartPadding(layoutDirection)
    val top = if (side == Side.Top) 0.dp else this.calculateTopPadding()
    val end = if (side == Side.End) 0.dp else this.calculateEndPadding(layoutDirection)
    val bottom = if (side == Side.Bottom) 0.dp else this.calculateBottomPadding()
    return PaddingValues(start = start, top = top, end = end, bottom = bottom)
}

fun PaddingValues.removeStartPadding(layoutDirection: LayoutDirection = LayoutDirection.Ltr): PaddingValues {
    return removePaddings(Side.Start, layoutDirection)
}

fun PaddingValues.removeTopPadding(): PaddingValues {
    return removePaddings(Side.Top)
}

fun PaddingValues.removeEndPadding(layoutDirection: LayoutDirection = LayoutDirection.Ltr): PaddingValues {
    return removePaddings(Side.End, layoutDirection)
}

fun PaddingValues.removeBottomPadding(): PaddingValues {
    return removePaddings(Side.Bottom)
}
