package com.example.testlockscreen.tile

import androidx.wear.tiles.RequestBuilders
import androidx.wear.tiles.ResourceBuilders
import androidx.wear.tiles.TileBuilders
import androidx.wear.tiles.TileService
import com.example.testlockscreen.presentation.MainActivity
import com.google.common.util.concurrent.Futures

// Imports for the new protolayout API
import androidx.wear.protolayout.ActionBuilders
import androidx.wear.protolayout.ColorBuilders
import androidx.wear.protolayout.DimensionBuilders
import androidx.wear.protolayout.LayoutElementBuilders
import androidx.wear.protolayout.TimelineBuilders
import androidx.wear.protolayout.ModifiersBuilders

class CadenseTileService : TileService() {
    override fun onTileRequest(requestParams: RequestBuilders.TileRequest) = 
        Futures.immediateFuture(
            TileBuilders.Tile.Builder()
                .setTileTimeline(
                    TimelineBuilders.Timeline.Builder()
                        .addTimelineEntry(
                            TimelineBuilders.TimelineEntry.Builder()
                                .setLayout(
                                    LayoutElementBuilders.Layout.Builder()
                                        .setRoot(
                                            LayoutElementBuilders.Box.Builder()
                                                .setWidth(DimensionBuilders.expand())
                                                .setHeight(DimensionBuilders.expand())
                                                .setHorizontalAlignment(LayoutElementBuilders.HORIZONTAL_ALIGN_CENTER)
                                                .setVerticalAlignment(LayoutElementBuilders.VERTICAL_ALIGN_CENTER)
                                                .setModifiers(
                                                    ModifiersBuilders.Modifiers.Builder()
                                                        .setClickable(
                                                            ModifiersBuilders.Clickable.Builder()
                                                                .setOnClick(
                                                                    ActionBuilders.LaunchAction.Builder()
                                                                        .setAndroidActivity(
                                                                            ActionBuilders.AndroidActivity.Builder()
                                                                                .setClassName(MainActivity::class.java.name)
                                                                                .setPackageName(this.packageName)
                                                                                .build()
                                                                        ).build()
                                                                )
                                                                .build()
                                                        )
                                                        .build()
                                                )
                                                .addContent(
                                                    LayoutElementBuilders.Column.Builder()
                                                        .setHorizontalAlignment(LayoutElementBuilders.HORIZONTAL_ALIGN_CENTER)
                                                        .addContent(
                                                            LayoutElementBuilders.Text.Builder()
                                                                .setText("Stride Rehabilitation")
                                                                .setFontStyle(
                                                                    LayoutElementBuilders.FontStyle.Builder()
                                                                        .setWeight(LayoutElementBuilders.FONT_WEIGHT_BOLD)
                                                                        .setSize(DimensionBuilders.sp(16f))
                                                                        .setColor(ColorBuilders.argb(0xFFFFFFFF.toInt()))
                                                                        .build()
                                                                )
                                                                .build()
                                                        )
                                                        .addContent(
                                                            LayoutElementBuilders.Text.Builder()
                                                                .setText("Tap to Enter Session")
                                                                .setFontStyle(
                                                                    LayoutElementBuilders.FontStyle.Builder()
                                                                        .setSize(DimensionBuilders.sp(14f))
                                                                        .setColor(ColorBuilders.argb(0xFFFFFFFF.toInt()))
                                                                        .build()
                                                                )
                                                                .build()
                                                        )
                                                        .build()
                                                )
                                                .build()
                                        )
                                        .build()
                                )
                                .build()
                        )
                        .build()
                )
                .build()
        )

    override fun onResourcesRequest(requestParams: RequestBuilders.ResourcesRequest) = 
        Futures.immediateFuture(
            ResourceBuilders.Resources.Builder().setVersion("1").build()
        )
}