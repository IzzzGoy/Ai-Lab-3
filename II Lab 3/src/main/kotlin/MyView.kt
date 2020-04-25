import frameModel.*
import javafx.beans.value.ObservableValue
import javafx.collections.FXCollections
import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.input.KeyCode
import javafx.scene.layout.BorderPane
import javafx.scene.layout.FlowPane
import javafx.scene.paint.Color
import javafx.stage.Modality
import javafx.stage.Stage
import library.PtrType
import library.Type
import tornadofx.*
import java.lang.Exception
import kotlin.math.E


class MyView : View() {
    override val root : Parent by fxml()
    val framePane : BorderPane by fxid()
    var tableFrame: TableView<Slot> by singleAssign()
    val addFrame : Button by fxid()
    val removeFrame : Button by fxid()
    val addSign : Button by fxid()
    val removeSign : Button by fxid()
    val addSlot : Button by fxid()
    val removeSlot : Button by fxid()
    var fromField : TextField? = null
    var toField : TextField? = null
    var rangePane : FlowPane? = null
    var frame = Frame("first")
    val rootFrame = frame
    val childFrames = FXCollections.observableArrayList<Slot>()
    val workersPane : BorderPane by fxid()
    var workersTable : TableView<PersonKt> by singleAssign()
    val persons = FXCollections.observableArrayList<PersonKt>()
    val addPerson : Button by fxid()
    val removePerson : Button by fxid()
    val searchingPane : BorderPane by fxid()

    init {
        val frameName = frame.name
        try {
            /*frame.addSlot(OneFieldSlot("Kill", PtrType.O, Type.INTEGER))
            frame.addSlot(OneFieldSlot("Years", PtrType.O, Type.TEXT, obj = "kill it!"))
            frame.addFrame("second")
            frame.addFrame("third")
            frame.getChildFrame(0).addSlot(OneFieldSlot("Fuck", PtrType.U, Type.INTEGER, obj = "18"))
            frame.getChildFrame(1).addSlot(TableSlot("kick", PtrType.U))
            (frame.getChildFrame(1).slots.last().obj as HashMap<String,String>)["Check"] = "Mate"
            //frame.slots.add(ListSlot<PersonKt>("Hui",ListSlotType.PERSON))
            frame.getChildFrame(0).addSlot(ListSlot<Int>("Huinia", ListSlotType.INT))*/
            frame.addFrame("Persons Storage")
            frame.addFrame("Persons List")
            frame.getChildFrame(1).addSlot(LispSlot("generatePersonsList",LispType.PRODUCE))
            (frame.getChildFrame(1).slots[0] as LispSlot).addArg("findPersonsList")
            frame.getChildFrame(0).addSlot(ListSlot<PersonKt>("Persons",ListSlotType.PERSON))
            frame.addSlot(LispSlot("findPersonsList",LispType.FIND))
            (frame.slots.last() as LispSlot).addArg("Persons Storage")
            (frame.slots.last() as LispSlot).addArg("Persons")

            frame.addSlot(LispSlot("getTargetEducation",LispType.FIND))
            (frame.slots.last() as LispSlot).addArg("targetFrame")
            (frame.slots.last() as LispSlot).addArg("Education")

            frame.addSlot(LispSlot("getTargetProfession",LispType.FIND))
            (frame.slots.last() as LispSlot).addArg("targetFrame")
            (frame.slots.last() as LispSlot).addArg("Profession")

            frame.addSlot(LispSlot("getTargetYears",LispType.FIND))
            (frame.slots.last() as LispSlot).addArg("targetFrame")
            (frame.slots.last() as LispSlot).addArg("Years")

            frame.getChildFrame(1).addSlot(LispSlot("findPersons",LispType.COMPARE))
            (frame.getChildFrame(1).slots[1] as LispSlot).addArg("getTargetEducation")
            (frame.getChildFrame(1).slots[1] as LispSlot).addArg("getTargetProfession")
            (frame.getChildFrame(1).slots[1] as LispSlot).addArg("getTargetYears")

            frame.refreshChildFrames()
        } catch (e: Exception){
            e.printStackTrace()
        }

        for (e in frame.slots){
            childFrames.add(e)
        }
        for (i in 0 until frame.childFramesCount()){
            childFrames.add(frame.getChildFrame(i))
        }


        framePane.center{
           vbox {
               var frameNameLabel = label("Frame name : $frameName"){
                   style{
                       fontSize = Dimension(50.0,Dimension.LinearUnits.px)
                   }
               }
               tableFrame = tableview(childFrames) {
                   readonlyColumn("name", Slot::name)
                   readonlyColumn("type", Slot::type)
                   readonlyColumn("ptrType",Slot::ptrType)
                   readonlyColumn("value",Slot::obj)
                   onKeyPressed = EventHandler {
                       if (it.code == KeyCode.ESCAPE){
                           if(frame.owner != null){
                               frame = frame.owner!!
                               frameNameLabel.text = "Frame name : ${frame.name}"
                               childFrames.clear()
                               for (e in frame.slots){
                                   childFrames.add(e)
                               }
                               for (i in 0 until frame.childFramesCount()){
                                   childFrames.add(frame.getChildFrame(i))
                               }
                               tableFrame.items = childFrames
                           }
                       }
                   }
                   onDoubleClick {
                       if (selectionModel.selectedItem is Frame){
                           frame = selectionModel.selectedItem as Frame
                           frameNameLabel.text = "Frame name : ${frame.name}"
                           childFrames.clear()
                           for (e in frame.slots){
                               childFrames.add(e)
                           }
                           for (i in 0 until frame.childFramesCount()){
                               childFrames.add(frame.getChildFrame(i))
                           }
                           tableFrame.items = childFrames
                       } else {
                           val stage = Stage()
                           val selectedSlot = selectionModel.selectedItem
                           when (selectionModel.selectedItem){
                               is OneFieldSlot -> {
                                   stage.apply {
                                        title = "Информация о слоте"
                                       scene = Scene( vbox {
                                           spacing = 25.0
                                           paddingAll = 25.0
                                           alignment = Pos.CENTER
                                           label ("Name : ${selectedSlot.name}"){
                                                style{
                                                    fontSize = Dimension(25.0,Dimension.LinearUnits.px)
                                                }
                                           }
                                           label ("Type : ${selectedSlot.type}"){
                                               style{
                                                   fontSize = Dimension(25.0,Dimension.LinearUnits.px)
                                               }
                                           }
                                           label ("PtrType : ${selectedSlot.ptrType}"){
                                               style{
                                                   fontSize = Dimension(25.0,Dimension.LinearUnits.px)
                                               }
                                           }
                                           label ("Value : ${selectedSlot.obj}"){
                                               style{
                                                   fontSize = Dimension(25.0,Dimension.LinearUnits.px)
                                               }
                                           }
                                           button ("Редактировать"){
                                               onMouseClicked = EventHandler {
                                                   stage.scene.root.apply{
                                                       getChildList()?.clear()
                                                       spacing = 25.0
                                                       paddingAll = 25.0
                                                       alignment = Pos.CENTER
                                                       val nameField = textfield {
                                                           isFocusTraversable = false
                                                           text = selectedSlot.name
                                                       }
                                                       val typeField = combobox<Type> {
                                                           for (item in Type.values()){
                                                               items.add(item)
                                                           }
                                                           value = selectedSlot.type
                                                           prefWidthProperty().bind(nameField.widthProperty())
                                                       }
                                                       val ptrTypeField = combobox<PtrType> {
                                                           for (item in PtrType.values()){
                                                               items.add(item)
                                                           }
                                                           value = selectedSlot.ptrType
                                                           prefWidthProperty().bind(nameField.widthProperty())
                                                       }
                                                       val valueField = textfield {
                                                           isFocusTraversable = false
                                                           text = selectedSlot.obj.toString()
                                                       }
                                                       val rangePane = flowpane{
                                                           isVisible = false
                                                           fromField = textfield {
                                                               maxWidth = 50.0
                                                               isFocusTraversable = false
                                                               promptText = "from"
                                                           }
                                                           toField = textfield {
                                                               maxWidth = 50.0
                                                               isFocusTraversable = false
                                                               promptText = "to"
                                                           }
                                                       }
                                                       ptrTypeField.valueProperty().addListener(ChangeListener { _: ObservableValue<out PtrType>?, _: PtrType, new: PtrType ->
                                                            if (new == PtrType.R){
                                                                valueField.isDisable = true
                                                                valueField.isVisible = false
                                                                rangePane.isDisable = false
                                                                rangePane.isVisible = true
                                                            } else {
                                                                valueField.isDisable = false
                                                                valueField.isVisible = true
                                                                rangePane.isDisable = true
                                                                rangePane.isVisible = false
                                                            }
                                                       })
                                                       stage.title = "Редактирование слота"
                                                       button ("Спасти и сохранить"){
                                                           prefWidthProperty().bind(nameField.widthProperty())
                                                           onMouseClicked = EventHandler {
                                                               if (checkInputSlotData(valueField.text, typeField.selectedItem!!)){
                                                                   for (s in frame.slots){
                                                                       stage.close()
                                                                       if (s == selectedSlot){
                                                                           s.delete()
                                                                           if (ptrTypeField.value == PtrType.R){
                                                                               if (fromField?.text?.isNotEmpty()!! && toField?.text?.isNotEmpty()!! && fromField?.text?.toInt()!! < toField?.text?.toInt()!!) {
                                                                                   frame.addSlot(
                                                                                       OneFieldSlot(
                                                                                           nameField.text,
                                                                                           ptrTypeField.selectedItem!!,
                                                                                           typeField.selectedItem!!,
                                                                                           obj = IntRange(fromField!!.text.toInt(), toField!!.text.toInt())
                                                                                       )
                                                                                   )
                                                                               }
                                                                           } else {
                                                                               frame.addSlot(OneFieldSlot(nameField.text,ptrTypeField.selectedItem!!,typeField.selectedItem!!, obj = valueField.text))
                                                                           }
                                                                           frame.refreshChildFrames()
                                                                           childFrames.clear()
                                                                           for (e in frame.slots){
                                                                               childFrames.add(e)
                                                                           }
                                                                           for (i in 0 until frame.childFramesCount()){
                                                                               childFrames.add(frame.getChildFrame(i))
                                                                           }
                                                                           tableFrame.items = childFrames
                                                                           break
                                                                       }
                                                                   }
                                                               }
                                                           }
                                                       }
                                                   }
                                               }
                                           }
                                       },250.0,300.0)
                                       initModality(Modality.APPLICATION_MODAL)
                                       showAndWait()
                                   }
                               }
                               is TableSlot -> {
                                   val modStage = Stage()
                                   modStage.apply {
                                       scene = Scene(vbox {
                                           alignment = Pos.CENTER
                                           paddingAll = 25.0
                                           spacing = 25.0
                                           val rows = FXCollections.observableArrayList<PairOf>()
                                           for ( k in (selectedSlot as TableSlot).getMap().keys){
                                               rows.add(selectedSlot.getMap()[k]?.let {
                                                   PairOf(k,it)
                                               })
                                           }
                                           val tab = tableview(rows){
                                               readonlyColumn("Keys",PairOf::key){
                                                   prefWidthProperty().bind(tableView.widthProperty().multiply(0.5))
                                               }
                                               readonlyColumn("Values",PairOf::value){
                                                   prefWidthProperty().bind(tableView.widthProperty().multiply(0.5))
                                               }
                                           }
                                           button ("Добавить"){
                                               onMouseClicked = EventHandler {
                                                   modStage.scene.root.apply {
                                                       getChildList()?.clear()
                                                       alignment = Pos.CENTER
                                                       spacing = 25.0
                                                       paddingAll = 25.0
                                                       val keyField = textfield {
                                                           promptText = "Key"
                                                           isFocusTraversable = false
                                                       }
                                                       val valueField = textfield {
                                                           promptText = "Value"
                                                           isFocusTraversable = false
                                                       }
                                                       button("Добавить") {
                                                           onMouseClicked = EventHandler {
                                                               selectedSlot.getMap()[keyField.text] = valueField.text
                                                               rows.clear()
                                                               for (k in selectedSlot.getMap().keys) {
                                                                   rows.add(selectedSlot.getMap()[k]?.let {
                                                                       PairOf(k, it)
                                                                   })
                                                               }
                                                               modStage.close()
                                                           }
                                                       }
                                                   }
                                               }
                                           }
                                           button ("Удалить"){
                                               onMouseClicked = EventHandler {
                                                   selectedSlot.getMap().remove(tab.selectionModel.selectedItem.key)
                                                   rows.clear()
                                                   for ( k in selectedSlot.getMap().keys){
                                                       rows.add(selectedSlot.getMap()[k]?.let {
                                                           PairOf(k,it)
                                                       })
                                                   }
                                               }
                                           }

                                       },250.0,400.0)

                                       initModality(Modality.APPLICATION_MODAL)
                                       showAndWait()
                                   }
                               }
                               is ListSlot<*> -> {
                                   stage.apply {
                                       scene = Scene(vbox {
                                           spacing = 25.0
                                           paddingAll = 25.0
                                           alignment = Pos.CENTER
                                           label("Name : ${selectedSlot.name}"){

                                           }
                                           val lisSlotView = listview<String> {
                                               for (e in (selectedSlot as ListSlot<*>).getList()){
                                                   items.add(e.toString())
                                               }
                                           }
                                           button("Добавить"){
                                               onMouseClicked = EventHandler {
                                                   selectedSlot as ListSlot<*>
                                                   stage.scene.root.apply {
                                                       getChildList()?.clear()
                                                       if (selectedSlot.checkType(ListSlotType.PERSON)) {
                                                           val fioField = textfield {
                                                               isFocusTraversable = false
                                                               promptText = " FIO"
                                                           }
                                                           val professionField = textfield {
                                                               isFocusTraversable = false
                                                               promptText = " Profession"
                                                           }
                                                           val educationField = textfield {
                                                               isFocusTraversable = false
                                                               promptText = " Education"
                                                           }
                                                           val yearsField = combobox<Int> {
                                                               for (i in 0..99){
                                                                   items.add(i)
                                                               }
                                                           }
                                                           button("Добавить"){
                                                               onMouseClicked = EventHandler {
                                                                   (selectedSlot.obj as ArrayList<PersonKt>).add(PersonKt(fioField.text,educationField.text,professionField.text,yearsField.value))
                                                                   lisSlotView.items.clear()
                                                                   for (e in selectedSlot.getList()){
                                                                       lisSlotView.items.add(e.toString())
                                                                   }
                                                                   stage.close()
                                                               }
                                                           }
                                                       }
                                                       else if(selectedSlot.checkType(ListSlotType.INT)){
                                                           val inputField = textfield {
                                                               filterInput { it.controlNewText.isInt() }
                                                           }
                                                           button("Добавить") {
                                                               onMouseClicked = EventHandler {
                                                                   selectedSlot as ListSlot<Int>
                                                                   selectedSlot.addElement(inputField.text.toInt())
                                                                   lisSlotView.items.clear()
                                                                   for (e in selectedSlot.getList()){
                                                                       lisSlotView.items.add(e.toString())
                                                                   }
                                                                   stage.close()
                                                               }
                                                           }
                                                       }
                                                       else if(selectedSlot.checkType(ListSlotType.DOUBLE)){
                                                           val inputField = textfield {
                                                               filterInput { it.controlNewText.isDouble() }
                                                           }
                                                           button("Добавить") {
                                                               onMouseClicked = EventHandler {
                                                                   selectedSlot as ListSlot<Double>
                                                                   selectedSlot.addElement(inputField.text.toDouble())
                                                                   lisSlotView.items.clear()
                                                                   for (e in selectedSlot.getList()){
                                                                       lisSlotView.items.add(e.toString())
                                                                   }
                                                                   stage.close()
                                                               }
                                                           }
                                                       }
                                                       else {
                                                           val inputField = textfield {
                                                               promptText = "Value"
                                                           }
                                                           button("Добавить") {
                                                               onMouseClicked = EventHandler {
                                                                   selectedSlot as ListSlot<String>
                                                                   selectedSlot.addElement(inputField.text)
                                                                   lisSlotView.items.clear()
                                                                   for (e in selectedSlot.getList()){
                                                                       lisSlotView.items.add(e)
                                                                   }
                                                                   stage.close()
                                                               }
                                                           }
                                                       }
                                                   }
                                               }
                                           }
                                       }, 250.0,450.0)
                                       initModality(Modality.APPLICATION_MODAL)
                                       showAndWait()
                                   }
                               }
                               is LispSlot -> {
                                   selectedSlot as LispSlot
                                   stage.apply {
                                       scene = Scene(vbox {
                                           spacing = 25.0
                                           paddingAll = 25.0
                                           alignment = Pos.CENTER
                                           label("Name : ${selectedSlot.name}"){}
                                           label (" Type : ${selectedSlot.getLispType()}")
                                           for (i in selectedSlot.getList().indices){
                                               label("${i}. value: ${selectedSlot.getList()[i]}")
                                           }
                                           button("Execute"){
                                               onMouseClicked = EventHandler {
                                                   selectedSlot.execute()
                                                   refreshFrame()
                                                   stage.close()
                                               }
                                           }
                                       },250.0,450.0)
                                       initModality(Modality.WINDOW_MODAL)
                                       showAndWait()
                                   }
                               }
                           }
                       }
                   }
               }
               addFrame.onMouseClicked = EventHandler {
                    val stage = Stage()
                    stage.apply {
                        title = "add Frame"
                        scene = Scene(vbox {
                            paddingAll = 25.0
                            spacing = 20.0
                            val frameNameField = textfield {
                            promptText = "Frame name :"
                                isFocusTraversable = false
                            }
                            button ("Добавить"){
                                prefWidthProperty().bind(frameNameField.widthProperty())
                                onMouseClicked = EventHandler{
                                    frame.addFrame(frameNameField.text)
                                    childFrames.clear()
                                    for (e in frame.slots){
                                        childFrames.add(e)
                                    }
                                    for (i in 0 until frame.childFramesCount()){
                                        childFrames.add(frame.getChildFrame(i))
                                    }
                                    tableFrame.items = childFrames
                                    stage.close()
                                }
                            }
                        },250.0,300.0)
                        initModality(Modality.APPLICATION_MODAL)
                        showAndWait()
                    }
                }
               removeFrame.onMouseClicked = EventHandler {
                   frame.removeChildFrame(tableFrame.selectionModel.selectedItem as Frame)
                   childFrames.clear()
                   for (e in frame.slots){
                       childFrames.add(e)
                   }
                   for (i in 0 until frame.childFramesCount()){
                       childFrames.add(frame.getChildFrame(i))
                   }
                   tableFrame.items = childFrames
               }
               addSlot.onMouseClicked = EventHandler {
                   val stage = Stage()
                   stage.apply {
                       scene = Scene(vbox {
                           spacing = 25.0
                           paddingAll = 25.0
                           alignment = Pos.CENTER
                           combobox<String> {
                               items = observableListOf("OneFieldSlot","TableSlot","ListSlot","LispSlot")
                               value = "Slot`s type"
                               valueProperty().addListener(ChangeListener<String> { _: ObservableValue<out String>?, _: String, new: String ->
                                   if (stage.scene.root.getChildList()?.size == 1)stage.scene.root.getChildList()?.add(1,vbox{})
                                   stage.scene.root.getChildList()?.set(1, vbox {
                                       spacing = 25.0
                                       paddingAll = 25.0
                                       alignment = Pos.CENTER
                                       when (new) {
                                           "OneFieldSlot" -> {
                                               val nameField = textfield {
                                                   isFocusTraversable = false
                                                   promptText = "Name"
                                               }
                                               val typeField = combobox<Type> {
                                                   value = Type.INTEGER
                                                   for (item in Type.values()){
                                                       items.add(item)
                                                   }
                                                   prefWidthProperty().bind(nameField.widthProperty())
                                               }
                                               val ptrTypeField = combobox<PtrType> {
                                                   value = PtrType.U
                                                   for (item in PtrType.values()){
                                                       items.add(item)
                                                   }
                                                   prefWidthProperty().bind(nameField.widthProperty())
                                               }
                                               val valueField = textfield {
                                                   isFocusTraversable = false
                                                   promptText = "Value"
                                               }
                                               rangePane = flowpane{
                                                   isVisible = false
                                                   fromField = textfield {
                                                       maxWidth = 50.0
                                                       isFocusTraversable = false
                                                       promptText = "from"
                                                   }
                                                   toField = textfield {
                                                       maxWidth = 50.0
                                                       isFocusTraversable = false
                                                       promptText = "to"
                                                   }
                                               }
                                               ptrTypeField.valueProperty().addListener(ChangeListener { _: ObservableValue<out PtrType>?, _: PtrType, new: PtrType ->
                                                   if (new == PtrType.R){
                                                       valueField.isDisable = true
                                                       valueField.isVisible = false
                                                       rangePane!!.isDisable = false
                                                       rangePane!!.isVisible = true
                                                   } else {
                                                       valueField.isDisable = false
                                                       valueField.isVisible = true
                                                       rangePane!!.isDisable = true
                                                       rangePane!!.isVisible = false
                                                   }
                                               })
                                               button ("Добавить"){
                                                   prefWidthProperty().bind(nameField.widthProperty())
                                                   onMouseClicked = EventHandler {
                                                       when (ptrTypeField.value) {
                                                           PtrType.R -> {
                                                               if (fromField?.text?.isNotEmpty()!! && toField?.text?.isNotEmpty()!! && fromField?.text?.toInt()!! < toField?.text?.toInt()!!) {
                                                                   frame.addSlot(
                                                                       OneFieldSlot(
                                                                           nameField.text,
                                                                           ptrTypeField.selectedItem!!,
                                                                           typeField.selectedItem!!,
                                                                           obj = IntRange(fromField!!.text.toInt(), toField!!.text.toInt())
                                                                       )
                                                                   )
                                                               }
                                                           }
                                                           else -> {
                                                               if (checkInputSlotData(valueField.text, typeField.selectedItem!!)) {
                                                                   frame.addSlot(OneFieldSlot(nameField.text,ptrTypeField.selectedItem!!,typeField.selectedItem!!, obj = valueField.text))
                                                               }
                                                           }
                                                       }
                                                       refreshFrame()
                                                       stage.close()
                                                   }
                                               }
                                           }
                                           "TableSlot" -> {
                                               val nameField = textfield {
                                                   promptText = "Name"
                                                   isFocusTraversable = false
                                               }
                                               button("Добавить"){
                                                   frame.addSlot(TableSlot(nameField.text,PtrType.S))
                                                   refreshFrame()
                                                   stage.close()
                                               }
                                           }
                                           "ListSlot" -> {
                                               val nameField = textfield {
                                                   promptText = "Name"
                                                   isFocusTraversable = false
                                               }
                                               val choise = combobox<String>{
                                                   items = observableListOf("Person","Text","Integer", "Real")
                                               }
                                               button("Добавить"){
                                                   when (choise.value){
                                                       "Person" ->{frame.addSlot(ListSlot<PersonKt>(nameField.text,ListSlotType.PERSON))}
                                                       "Integer"->{frame.addSlot(ListSlot<Int>(nameField.text,ListSlotType.INT))}
                                                       "Real"->{frame.addSlot(ListSlot<Double>(nameField.text,ListSlotType.DOUBLE))}
                                                       else ->{frame.addSlot(ListSlot<String>(nameField.text,ListSlotType.TEXT))}
                                                   }
                                                   refreshFrame()
                                                   stage.close()
                                               }
                                           }
                                           else -> {
                                                combobox<LispType> {
                                                    items.addAll(LispType.COMPARE, LispType.FIND, LispType.PRODUCE)
                                                    value = LispType.FIND
                                                    if (stage.scene.root.getChildList()?.size!! < 3) stage.scene.root.add(vbox {})
                                                    stage.scene.root.getChildList()!![2] = vbox {
                                                        val nameField = textfield {
                                                            promptText = "Name"
                                                            isFocusTraversable = false
                                                        }
                                                        val firstArgField = textfield {
                                                            promptText = "Where"
                                                            isFocusTraversable = false
                                                        }
                                                        val secondArgField = textfield {
                                                            promptText = "What"
                                                            isFocusTraversable = false
                                                        }
                                                        button("Добавить"){
                                                            onMouseClicked = EventHandler {
                                                                val lisp = LispSlot(nameField.text,value)
                                                                lisp.addArg(firstArgField.text)
                                                                lisp.addArg(secondArgField.text)
                                                                frame.addSlot(lisp)
                                                                refreshFrame()
                                                                stage.close()
                                                            }
                                                        }
                                                    }

                                                    valueProperty().addListener(ChangeListener { _, _, new ->
                                                        run {
                                                            if (new == LispType.FIND) {
                                                                stage.scene.root.getChildList()!![2] = vbox {
                                                                    val nameField = textfield {
                                                                        promptText = "Name"
                                                                        isFocusTraversable = false
                                                                    }
                                                                    val firstArgField = textfield {
                                                                        promptText = "First arg"
                                                                        isFocusTraversable = false
                                                                    }
                                                                    val secondArgField = textfield {
                                                                        promptText = "Second arg"
                                                                        isFocusTraversable = false
                                                                    }

                                                                    button("Добавить"){
                                                                        onMouseClicked = EventHandler {
                                                                            val lisp = LispSlot(nameField.text,new)
                                                                            lisp.addArg(firstArgField.text)
                                                                            lisp.addArg(secondArgField.text)
                                                                            frame.addSlot(lisp)
                                                                            refreshFrame()
                                                                            stage.close()
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                            else if (new == LispType.PRODUCE){
                                                                stage.scene.root.getChildList()!![2] = vbox {
                                                                    val nameField = textfield {
                                                                        promptText = "Name"
                                                                        isFocusTraversable = false
                                                                    }
                                                                    val firstArgField = textfield {
                                                                        promptText = "First arg"
                                                                        isFocusTraversable = false
                                                                    }
                                                                    button("Добавить"){
                                                                        onMouseClicked = EventHandler {
                                                                            val lisp = LispSlot(nameField.text,new)
                                                                            lisp.addArg(firstArgField.text)
                                                                            frame.addSlot(lisp)
                                                                            refreshFrame()
                                                                            stage.close()
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                            else if (new == LispType.COMPARE){
                                                                stage.scene.root.getChildList()!![2] = vbox {
                                                                    val nameField = textfield {
                                                                        promptText = "Name"
                                                                        isFocusTraversable = false
                                                                    }
                                                                    val firstArgField = textfield {
                                                                        promptText = "First arg"
                                                                        isFocusTraversable = false
                                                                    }
                                                                    val secondArgField = textfield {
                                                                        promptText = "Second arg"
                                                                        isFocusTraversable = false
                                                                    }
                                                                    val thirdArgField = textfield {
                                                                        promptText = "Third arg"
                                                                        isFocusTraversable = false
                                                                    }
                                                                    /*val foursArgField = textfield {
                                                                        promptText = "Fours arg"
                                                                        isFocusTraversable = false
                                                                    }*/
                                                                    button("Добавить"){
                                                                        onMouseClicked = EventHandler {
                                                                            val lisp = LispSlot(nameField.text,new)
                                                                            lisp.addArg(firstArgField.text)
                                                                            lisp.addArg(secondArgField.text)
                                                                            lisp.addArg(thirdArgField.text)
                                                                            //lisp.addArg(foursArgField.text)
                                                                            frame.addSlot(lisp)
                                                                            refreshFrame()
                                                                            stage.close()
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                            else {
                                                                stage.scene.root.getChildList()!![2] = vbox {}
                                                            }
                                                        }
                                                    })
                                                }
                                           }
                                       }
                                   })
                               })
                           }
                       },250.0,400.0)
                       initModality(Modality.APPLICATION_MODAL)
                       showAndWait()
                   }
               }
               removeSlot.onMouseClicked = EventHandler {
                   if (tableFrame.selectionModel.selectedItem !is Frame){
                       tableFrame.selectionModel.selectedItem.delete()
                       frame.refreshChildFrames()
                       childFrames.clear()
                       for (e in frame.slots){
                           childFrames.add(e)
                       }
                       for (i in 0 until frame.childFramesCount()){
                           childFrames.add(frame.getChildFrame(i))
                       }
                       tableFrame.items = childFrames
                   }
               }
           }
        }
        workersPane.center{
             workersTable = tableview<PersonKt> {
                 prefWidth = 400.0
                 readonlyColumn("FIO", PersonKt::fio)
                 readonlyColumn("Education",PersonKt::education)
                 readonlyColumn("Profession",PersonKt::profession)
                 readonlyColumn("Years",PersonKt::years)
                 items = persons
            }
        }
        addPerson.onMouseClicked = EventHandler {
            val s = Stage()
            s.apply {
                scene = Scene(vbox {
                    spacing = 25.0
                    paddingAll = 25.0
                    alignment = Pos.CENTER
                    val fioField = textfield {
                        promptText = "FIO"
                        isFocusTraversable = false
                    }
                    val educationField = combobox<String> {
                        value = "Education"
                        items = observableListOf("Начальное","Среднее","Высшее")
                        prefWidthProperty().bind(fioField.widthProperty())
                    }
                    val professionField = combobox<String> {
                        value = "Profession"
                        items = observableListOf("Слесарь","Профессор", "Охранник", "Менеджер")
                        prefWidthProperty().bind(fioField.widthProperty())
                    }
                    val yearsField = combobox<Int> {
                        value = 1
                        for (i in 1 until 99){
                            items.add(i)
                        }
                        prefWidthProperty().bind(fioField.widthProperty())
                    }
                    button("Добавить"){
                        onMouseClicked = EventHandler {
                            (rootFrame.execute("findPersonsList") as ListSlot<PersonKt>).apply {
                                addElement(PersonKt(fioField.text,educationField.value,professionField.value, yearsField.value))
                                workersTable.items = getList().asObservable()
                                s.close()
                            }
                        }
                    }
                }, 250.0,450.0)
                initModality(Modality.WINDOW_MODAL)
                showAndWait()
            }
        }
        removePerson.onMouseClicked = EventHandler {
            (rootFrame.execute("findPersonsList") as ListSlot<PersonKt>).apply {
                removeElement(workersTable.selectionModel.selectedItem)
                workersTable.items = getList().asObservable()
            }
        }

        searchingPane.center{
            vbox {
                spacing = 25.0
                paddingAll = 25
                alignment = Pos.CENTER
                label("Выберите предпочтительное образование кандидата")
                val educationField = combobox<String> {
                    value = "Education"
                    items = observableListOf("Начальное","Среднее","Высшее")
                    prefWidth = 100.0
                }
                label("Выберите его профессию")
                val professionField = combobox<String> {
                    value = "Profession"
                    items = observableListOf("Слесарь","Профессор", "Охранник", "Менеджер")
                    prefWidthProperty().bind(educationField.widthProperty())
                }
                label("И его стаж")
                var from : ComboBox<Int>? = null
                var to : ComboBox<Int>? = null
                hbox {
                    alignment = Pos.CENTER
                    spacing = 25.0
                    from = combobox {
                        value = 1
                        for (i in 1 until 99){
                            items.add(i)
                        }
                    }
                    to = combobox {
                        value = 1
                        for (i in 1 until 99){
                            items.add(i)
                        }
                        valueProperty().addListener(ChangeListener { _, _, newValue ->
                            if (newValue < from?.value!!){
                                value = from?.value
                            }
                        })
                    }
                }
                addSign.onMouseClicked = EventHandler {
                    for (i in 0 until rootFrame.childFramesCount()){
                        if (rootFrame.getChildFrame(i).name == "targetFrame"){
                            rootFrame.getChildFrame(i).slots.clear()
                        }
                    }
                    rootFrame.addFrame("targetFrame")
                    if (educationField.value == "Education") {rootFrame.getChildFrame(2).addSlot(OneFieldSlot("Education",PtrType.U,Type.TEXT))}
                    else rootFrame.getChildFrame(2).addSlot(OneFieldSlot("Education",PtrType.U,Type.TEXT, obj = educationField.value))

                    if (professionField.value == "Profession") {rootFrame.getChildFrame(2).addSlot(OneFieldSlot("Profession",PtrType.U,Type.TEXT))}
                    else rootFrame.getChildFrame(2).addSlot(OneFieldSlot("Profession",PtrType.U,Type.TEXT, obj = professionField.value))

                    rootFrame.getChildFrame(2).addSlot(OneFieldSlot("Years",PtrType.R,Type.INTEGER, obj = IntRange(from?.value!!,to?.value!!)))
                    rootFrame.refreshChildFrames()
                    refreshFrame()
                }
            }
        }


        for (e in workersTable.columns){
            e.prefWidthProperty().bind(workersTable.widthProperty().multiply(0.25))
        }
    }
    private fun refreshFrame() {
        frame.refreshChildFrames()
        childFrames.clear()
        for (e in frame.slots){
            childFrames.add(e)
        }
        for (i in 0 until frame.childFramesCount()){
            childFrames.add(frame.getChildFrame(i))
        }

        tableFrame.items = childFrames
    }
    private fun checkInputSlotData(value : String, type: Type):Boolean{
        when(type){
            Type.INTEGER -> {
                try {
                    value.toInt()
                } catch (e : Exception){
                    return false
                }
                return true
            }
            Type.REAL -> {
                try {
                    value.toDouble()
                } catch (e : Exception){
                    return false
                }
                return true
            }
            Type.BOOL -> {
                try {
                    value.toBoolean()
                } catch (e : Exception){
                    return false
                }
                return true
            }
            Type.TEXT -> { return true }
            else -> return false
        }
    }

}
class PairOf (val key:String, val value: String){
}