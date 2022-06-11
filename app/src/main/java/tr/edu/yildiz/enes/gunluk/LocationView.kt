package tr.edu.yildiz.enes.gunluk

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.mapbox.geojson.Point
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.ViewAnnotationAnchor
import com.mapbox.maps.extension.style.layers.properties.generated.IconAnchor
import com.mapbox.maps.plugin.annotation.Annotation
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.*
import com.mapbox.maps.viewannotation.viewAnnotationOptions
import tr.edu.yildiz.enes.gunluk.databinding.ActivityViewAnnotationShowcaseBinding
import tr.edu.yildiz.enes.gunluk.databinding.ItemCalloutViewBinding

class LocationView : AppCompatActivity() {
    private lateinit var pointAnnotationManager: PointAnnotationManager
    private lateinit var pointAnnotation: PointAnnotation
    private lateinit var viewAnnotation: View

    var titles: ArrayList<String> = ArrayList()
    var boylams: ArrayList<String> = ArrayList()
    var enlems: ArrayList<String> = ArrayList()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityViewAnnotationShowcaseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        titles = intent.getSerializableExtra("title") as ArrayList<String>
        boylams = intent.getSerializableExtra("boylam") as ArrayList<String>
        enlems = intent.getSerializableExtra("enlem") as ArrayList<String>
        val iconBitmap = BitmapUtils.bitmapFromDrawableRes(
            this@LocationView,
            R.drawable.red_marker2
        )!!

        for (i in 0..titles.size-1){
            binding.mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS) {
                prepareAnnotationMarker(binding.mapView, iconBitmap, Point.fromLngLat(boylams.get(i).toDouble(), enlems.get(i).toDouble()))
                prepareViewAnnotation(binding.mapView, Point.fromLngLat(boylams.get(i).toDouble(), enlems.get(i).toDouble()), titles.get(i))
                pointAnnotationManager.addClickListener { clickedAnnotation ->
                    if (pointAnnotation == clickedAnnotation) {
                        viewAnnotation.toggleViewVisibility()
                    }
                    true
                }

                pointAnnotationManager.addDragListener(object : OnPointAnnotationDragListener {
                    override fun onAnnotationDragStarted(annotation: Annotation<*>) {
                    }

                    override fun onAnnotationDrag(annotation: Annotation<*>) {
                        if (annotation == pointAnnotation) {
                            binding.mapView.viewAnnotationManager.updateViewAnnotation(
                                viewAnnotation,
                                viewAnnotationOptions {
                                    geometry(pointAnnotation.geometry)
                                }
                            )
                            ItemCalloutViewBinding.bind(viewAnnotation).apply {
                                textNativeView.text = "%s".format(
                                    titles.get(i)
                                )
                            }
                        }
                    }

                    override fun onAnnotationDragFinished(annotation: Annotation<*>) {
                    }
                })
            }
        }

    }

    private fun View.toggleViewVisibility() {
        visibility = if (visibility == View.VISIBLE) View.GONE else View.VISIBLE
    }

    @SuppressLint("SetTextI18n")
    private fun prepareViewAnnotation(mapView: MapView, point: Point, title: String) {
        val viewAnnotationManager = mapView.viewAnnotationManager
        viewAnnotation = viewAnnotationManager.addViewAnnotation(
            resId = R.layout.item_callout_view,
            options = viewAnnotationOptions {
                geometry(point)
                associatedFeatureId(pointAnnotation.featureIdentifier)
                anchor(ViewAnnotationAnchor.BOTTOM)
                offsetY((pointAnnotation.iconImageBitmap?.height!!).toInt())
            }
        )
        ItemCalloutViewBinding.bind(viewAnnotation).apply {
            textNativeView.text = "%s".format(
                title
            )
        }
    }

    private fun prepareAnnotationMarker(mapView: MapView, iconBitmap: Bitmap, point: Point) {
        val annotationPlugin = mapView.annotations
        val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
            .withPoint(point)
            .withIconImage(iconBitmap)
            .withIconAnchor(IconAnchor.BOTTOM)
            .withDraggable(true)
        pointAnnotationManager = annotationPlugin.createPointAnnotationManager()
        pointAnnotation = pointAnnotationManager.create(pointAnnotationOptions)
    }


}