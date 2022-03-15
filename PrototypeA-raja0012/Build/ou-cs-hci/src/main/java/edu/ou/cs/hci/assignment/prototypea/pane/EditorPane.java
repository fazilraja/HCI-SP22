//******************************************************************************
// Copyright (C) 2019 University of Oklahoma Board of Trustees.
//******************************************************************************
// Last modified: Tue Jan 28 09:28:34 2020 by Chris Weaver
//******************************************************************************
// Major Modification History:
//
// 20190203 [weaver]:	Original file.
// 20190220 [weaver]:	Adapted from swingmvc to fxmvc.
//
//******************************************************************************
//
//******************************************************************************

package edu.ou.cs.hci.assignment.prototypea.pane;

import java.io.File;
import java.lang.*;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.animation.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.util.Callback;
import edu.ou.cs.hci.assignment.prototypea.Controller;
import edu.ou.cs.hci.resources.Resources;

//******************************************************************************

/**
 * The <CODE>EditorPane</CODE> class.
 *
 * @author  Chris Weaver
 * @version %I%, %G%
 */
public final class EditorPane extends AbstractPane
{
	private static final double	W = 200;		// poster icon width
	private static final double	H = W * 1.5;	// poster icon height
	//**********************************************************************
	// Private Class Members
	//**********************************************************************

	private static final String	NAME = "Editor";
	private static final String	HINT = "Movie Metadata Editor";

	//**********************************************************************
	// Private Class Members (Effects)
	//**********************************************************************

	private static final Font		FONT_LARGE =
		Font.font("Serif", FontPosture.ITALIC, 24.0);

	private static final Font		FONT_SMALL =
		Font.font("Serif", FontPosture.ITALIC, 18.0);

	//**********************************************************************
	// Private Members
	//**********************************************************************

	// Layout (a few widgets)

	private TextField  				title;//title

	private Spinner<Integer>		year;//year

	private Spinner<Float>			score;//score

	//awards
	private CheckBox				picture;
	private CheckBox				directing;
	private	CheckBox				cinema;
	private CheckBox				acting;

	private CheckBox 				action;
	private CheckBox 				comedy;
	private CheckBox 				documentary;
	private CheckBox 				drama;
	private CheckBox 				fantasy;
	private CheckBox 				horror;
	private CheckBox 				romance;
	private CheckBox 				scifi;
	private CheckBox 				thriller;
	private CheckBox				western;

	private TextArea				comment;//comments

	private TextField				director;//director

	private CheckBox				animated;//animated
	private CheckBox				color;//color

	private Spinner<Integer>		reviews;//reviews

	private ComboBox<String>			rating;//rating

	private Slider					runtime;//runtime
	private Label					runtimeValue;//label showing the value

	private TextArea				summary;//summary

	private Slider					slider;

	private Spinner<Integer>		spinner;

	private TextField				textField;

	private boolean					ignoreCaretEvents;

	private Button					chooseFile;
	private FileChooser				fileChooser;
	private Label					filePath;
	private Stage					fileStage;
	private ImageView 				view;
	private Image					image;

	// Handlers
	private final ActionHandler	actionHandler;

	//**********************************************************************
	// Constructors and Finalizer
	//**********************************************************************

	public EditorPane(Controller controller)
	{
		super(controller, NAME, HINT);

		actionHandler = new ActionHandler();

		setBase(buildPane());
	}

	//**********************************************************************
	// Public Methods (Controller)
	//**********************************************************************

	// TODO #4: Write code to initialize the widgets in your layout with the
	// data attribute values from the model when the UI first appears.

	// The controller calls this method when it adds a view.
	// Set up the nodes in the view with data accessed through the controller.
	public void	initialize()
	{
		//Title, Text Field
		title.setText((String)controller.get("title"));

		//Year, Spinner
		year.getValueFactory().setValue((Integer)controller.get("year"));

		//Score, Spinner
		score.getValueFactory().setValue((Float)controller.get("score"));

		//Rating, ComboBox
		rating.getSelectionModel().select((String)controller.get("rating"));

		//Runtime, Slider
		runtime.setValue((Integer)controller.get("runtime"));

		//director, textField
		director.setText((String)controller.get("title"));

		//animated, Checkbox
		animated.setSelected((Boolean)controller.get("animated"));

		//color, Checkbox
		color.setSelected((Boolean)controller.get("color"));

		//Summary, Textarea
		summary.setText((String)controller.get("summary"));

		//Genre, Checkboxes
		action.setSelected((Boolean)controller.get("action"));
		comedy.setSelected((Boolean)controller.get("comedy"));
		documentary.setSelected((Boolean)controller.get("documentary"));
		drama.setSelected((Boolean)controller.get("drama"));
		fantasy.setSelected((Boolean)controller.get("fantasy"));
		horror.setSelected((Boolean)controller.get("horror"));
		romance.setSelected((Boolean)controller.get("romance"));
		scifi.setSelected((Boolean)controller.get("scifi"));
		thriller.setSelected((Boolean)controller.get("thriller"));
		western.setSelected((Boolean)controller.get("western"));

		//Awards
		picture.setSelected((Boolean)controller.get("picture"));
		directing.setSelected((Boolean)controller.get("directing"));
		cinema.setSelected((Boolean)controller.get("cinema"));
		acting.setSelected((Boolean)controller.get("acting"));

		//comments
		comment.setText((String)controller.get("comment"));
	}

	// TODO #5: Write code to detach widgets from any model properties (or other
	// resources) it has been using, in preparation for removing and destroying
	// the widget object. For Prototype A there's nothing to do for this step
	// since we only detach a widget when its window closes or the program ends.

	// TODO #6: Write code to remove widgets from the layout hierarchy. For
	// Prototype A there's nothing to do for this step since we only remove a
	// widget when its window closes or the program ends, and in those cases
	// JavaFX does the necessary cleanup automatically.

	// TODO #7: Write code to unregister each widget from any event listeners
	// and/or property change handlers it was registered with in TODO #2.
	
	// TODO #8: Write code to actually destroy the widget objects. There is
	// nothing to do here (in Prototype A or otherwise) since Java uses garbage
	// collection to destroy objects and reclaim any memory allocated for them.

	// The controller calls this method when it removes a view.
	// Unregister event and property listeners for the nodes in the view.
	public void	terminate()
	{
		//Title, Text Field
		title.setOnAction(null);

		//Year, Spinner
		year.valueProperty().removeListener(this::changeInteger);

		//Score, Spinner
		score.valueProperty().removeListener(this::changeDecimal);

		//Rating, ComboBox
		rating.getSelectionModel().selectedItemProperty().removeListener(
				this::changeItem);

		//Runtime, Slider
		runtime.valueProperty().removeListener(this::changeRuntime);

		//director
		director.setOnAction(null);

		//animated
		animated.setOnAction(null);

		//color
		color.setOnAction(null);

		//Genres
		action.setOnAction(null);
		comedy.setOnAction(null);
		documentary.setOnAction(null);
		drama.setOnAction(null);
		fantasy.setOnAction(null);
		horror.setOnAction(null);
		romance.setOnAction(null);
		scifi.setOnAction(null);
		thriller.setOnAction(null);
		western.setOnAction(null);

		//Awards
		picture.setOnAction(null);
		directing.setOnAction(null);
		cinema.setOnAction(null);
		acting.setOnAction(null);

		//summary
		summary.caretPositionProperty().removeListener(this::changeCaret);

		//comment
		comment.caretPositionProperty().removeListener(this::changeCaret);

	}

	// TODO #10: Write code to take any changes to data attribute values in the
	// model and update the corresponding widgets. The Model and Controller
	// classes help by calling the update() method below whenever there is a
	// data attribute value. Use the same key (name) from TODO #0 to figure out
	// which data attribute has changed, and update the corresponding widget to
	// show the new value.

	// The controller calls this method whenever something changes in the model.
	// Update the nodes in the view to reflect the change.
	public void	update(String key, Object value)
	{
		//System.out.println("update " + key + " to " + value);

		if ("myDouble".equals(key))
			slider.setValue((Double)value);
		else if ("myInt".equals(key))
			spinner.getValueFactory().setValue((Integer)value);
		else if ("myString".equals(key))
			textField.setText((String)value);
		//title
		else if("title".equals(key))
			title.setText((String)value);
		//score
		else if("score".equals(key))
			score.getValueFactory().setValue((Float)value);
		//year
		else if("year".equals(key))
			year.getValueFactory().setValue((Integer)value);
		//rating
		else if("rating".equals(key))
			rating.getSelectionModel().select((String)value);
		//runtime
		else if("runtime".equals(key))
			runtime.setValue((Integer)value);
		//director
		else if("director".equals(key))
			director.setText((String)value);
		//animated
		else if("animated".equals(key))
			animated.setSelected((Boolean)value);
		//color
		else if("color".equals(key))
			color.setSelected((Boolean)value);
		//summary
		else if("summary".equals(key)) {
			ignoreCaretEvents = true;
			int caret = summary.getCaretPosition();
			summary.setText((String)value);
			summary.positionCaret(caret);
			ignoreCaretEvents = false;
		}

		//Genre
		else if("action".equals(key))
			color.setSelected((Boolean)value);
		else if("comedy".equals(key))
			color.setSelected((Boolean)value);
		else if("documentary".equals(key))
			color.setSelected((Boolean)value);
		else if("drama".equals(key))
			color.setSelected((Boolean)value);
		else if("fantasy".equals(key))
			color.setSelected((Boolean)value);
		else if("horror".equals(key))
			color.setSelected((Boolean)value);
		else if("romance".equals(key))
			color.setSelected((Boolean)value);
		else if("scifi".equals(key))
			color.setSelected((Boolean)value);
		else if("thriller".equals(key))
			color.setSelected((Boolean)value);
		else if("western".equals(key))
			color.setSelected((Boolean)value);

		//awards
		else if("picture".equals(key))
			picture.setSelected((Boolean)value);
		else if("directing".equals(key))
			directing.setSelected((Boolean)value);
		else if("cinema".equals(key))
			cinema.setSelected((Boolean)value);
		else if("acting".equals(key))
			acting.setSelected((Boolean)value);

		//comment
		else if("comment".equals(key)) {
			ignoreCaretEvents = true;
			int caret = comment.getCaretPosition();
			comment.setText((String)value);
			comment.positionCaret(caret);
			ignoreCaretEvents = false;
		}
	}

	//**********************************************************************
	// Private Methods (Layout)
	//**********************************************************************

	// TODO #3: Write code to organize the widgets, labels, etc. in your design
	// into a hierarchical layout of panes. Refer to the javafx.scene.layout
	// package in the JavaFX APIs to learn about available pane classes. You are
	// likely to find BorderPane, GridPane, HBox, StackPane, VBox most useful. 

	private Pane	buildPane()
	{
		//have VBoxes as rows and VBoxes as columns, add final boxes into a HBox
		HBox row1 = new HBox();
		row1.getChildren().add(createTitle());
		row1.setSpacing(10);
		//row1.setAlignment(Pos.CENTER_RIGHT);

		HBox row2 = new HBox();
		row2.getChildren().add(createYear());
		row2.getChildren().add(createScore());
		row2.getChildren().add(createReviews());
		row2.getChildren().add(createRating());
		row2.getChildren().add(createRuntime());
		row2.setSpacing(7);
		row2.fillHeightProperty();
		//row2.setAlignment(Pos.CENTER_RIGHT);

		HBox row3 = new HBox();
		row3.getChildren().add(createDirector());
		row3.getChildren().add(createAniColor());
		row3.setSpacing(7);
		//row3.setAlignment(Pos.CENTER_RIGHT);

		HBox row4 = new HBox();
		row4.getChildren().add(createSummary());
		//row4.setAlignment(Pos.CENTER_RIGHT);

		HBox row5 = new HBox();
		row5.getChildren().add(createGenre());
		//row5.setAlignment(Pos.CENTER_RIGHT);

		HBox row6 = new HBox();
		row6.getChildren().add(createAwards());
		//row6.setAlignment(Pos.CENTER_RIGHT);

		HBox row7 = new HBox();
		row7.getChildren().add(createComment());
		//row7.setAlignment(Pos.CENTER_RIGHT);

		VBox c1 = new VBox();
		VBox.setMargin(c1, new Insets(4, 50, 10, 5));

		c1.setSpacing(10);
		c1.getChildren().add(row1);
		c1.getChildren().add(row2);
		c1.getChildren().add(row3);
		c1.getChildren().add(row4);
		c1.getChildren().add(row5);
		c1.getChildren().add(row6);
		c1.getChildren().add(row7);

		VBox c2 = new VBox();
		c2.getChildren().add(createPoster());
		c2.setAlignment(Pos.CENTER_LEFT);
		c2.setSpacing(5);

		//root node
		HBox root = new HBox();
		root.getChildren().add(c2);
		root.getChildren().add(c1);

		return root;
	}

	//**********************************************************************
	// Private Methods (Widget Pane Creators)
	//**********************************************************************

	// TODO #1: Write methods to create the widgets used for editing,
	// showing, labeling, etc. the various data attributes in the model.
	// (Note that the example methods below put their widget inside a titled
	// pane and return that pane instead of the widget itself. For your design,
	// you probably won't want to box and label your widgets that way.)

	//create title
	private TextField	createTitle()
	{
		title = new TextField();

		title.setPrefColumnCount(10);

		title.setOnAction(actionHandler);

		//text
		title.setText("Title");
		title.setFont(Font.font("Serif", FontPosture.REGULAR, 24.0));

		return title;
	}

	//score
	private Pane	createScore()
	{
		score = new Spinner<Float>(0.0, 10.0, 5.0, 0.1);

		score.setEditable(true);
		score.getEditor().setPrefColumnCount(4);

		score.valueProperty().addListener(this::changeDecimal);

		return createTitledPane(score, "Score");
	}

	//year
	private Pane	createYear()
	{
		year = new Spinner<Integer>(1940, 2040, 2020, 1);

		year.setEditable(true);
		year.getEditor().setPrefColumnCount(5);

		year.valueProperty().addListener(this::changeInteger);

		return createTitledPane(year, "Year");
	}

	//reviews
	private Pane	createReviews()
	{
		reviews = new Spinner<Integer>(0, 1000000, 0, 1);

		reviews.setEditable(true);
		reviews.getEditor().setPrefColumnCount(4);

		reviews.valueProperty().addListener(this::changeInteger);

		return createTitledPane(reviews, "# of Reviews");
	}

	//rating
	private Pane createRating()
	{
		ArrayList<String> ratings = new ArrayList<String>(Arrays.asList("G", "PG", "PG-13", "R"));
		rating = new ComboBox<String>();
		rating.getItems().addAll(ratings);

		rating.setEditable(false);
		rating.setVisibleRowCount(4);

		rating.getSelectionModel().selectedItemProperty().addListener(
				this::changeItem);

		return createTitledPane(rating, "Rating");
	}

	//runtime
	private Pane	createRuntime()
	{
		runtime = new Slider(1, 360, 1);

		runtimeValue = new Label(" ");

		//major tick every 60 minutes
		runtime.setOrientation(Orientation.HORIZONTAL);
		runtime.setBlockIncrement(1);
		runtime.setMajorTickUnit(60);
		runtime.setMinorTickCount(30);
		runtime.setShowTickLabels(true);
		runtime.setShowTickMarks(true);

		runtime.valueProperty().addListener(this::changeRuntime);

		return createTitledPane(runtime, "Runtime");
	}

	private Pane createDirector()
	{
		Label directedBy = new Label("Directed by");
		HBox directed = new HBox();
		directed.setSpacing(3);

		director = new TextField();

		director.setPrefColumnCount(10);

		director.setOnAction(actionHandler);
		directed.getChildren().addAll(directedBy, director);

		return directed;
	}

	private Pane createAniColor()
	{
		animated = new CheckBox("is animated");
		color = new CheckBox("is colored");

		animated.setOnAction(actionHandler);
		color.setOnAction(actionHandler);

		HBox checkBoxes1 = new HBox();
		checkBoxes1.getChildren().add(animated);
		checkBoxes1.getChildren().add(color);
		checkBoxes1.setSpacing(7);

		HBox.setMargin(checkBoxes1, new Insets(0, 5, 0, 5));

		return checkBoxes1;
	}

	private Pane createSummary() {

		summary = new TextArea();

		summary.setPrefRowCount(7);
		summary.setPrefColumnCount(35);

		summary.caretPositionProperty().addListener(this::changeCaret);

		return createTitledPane(summary, "Summary");

	}

	private Pane createGenre() {

		action = new CheckBox("Action");
		comedy = new CheckBox("Comedy");
		documentary = new CheckBox("Documentary");
		drama = new CheckBox("Drama");
		fantasy = new CheckBox("Fantasy");
		horror = new CheckBox("Horror");
		romance = new CheckBox("Romance");
		scifi = new CheckBox("Sci-fi");
		thriller = new CheckBox("Thriller");
		western = new CheckBox("Western");

		action.setOnAction(actionHandler);
		comedy.setOnAction(actionHandler);
		documentary.setOnAction(actionHandler);
		drama.setOnAction(actionHandler);
		fantasy.setOnAction(actionHandler);
		horror.setOnAction(actionHandler);
		romance.setOnAction(actionHandler);
		scifi.setOnAction(actionHandler);
		thriller.setOnAction(actionHandler);
		western.setOnAction(actionHandler);

		HBox r1 = new HBox();
		r1.getChildren().addAll(action, comedy, documentary, drama, fantasy);
		r1.setSpacing(5);

		HBox r2 = new HBox();
		r2.getChildren().addAll(horror, romance, scifi, thriller, western);
		r2.setSpacing(5);

		VBox root = new VBox();
		root.getChildren().add(r1);
		root.getChildren().add(r2);
		root.setSpacing(4);

		return createTitledPane(root, "Genres");
	}

	private Pane createAwards() {

		picture= new CheckBox("Picture");
		directing = new CheckBox("Directing");
		cinema = new CheckBox("Cinematography");
		acting = new CheckBox("Acting");

		picture.setOnAction(actionHandler);
		directing.setOnAction(actionHandler);
		cinema.setOnAction(actionHandler);
		acting.setOnAction(actionHandler);

		HBox root = new HBox();
		root.setSpacing(5);

		root.getChildren().add(picture);
		root.getChildren().add(directing);
		root.getChildren().add(cinema);
		root.getChildren().add(acting);

		return createTitledPane(root, "Awards");

	}

	private Pane createComment() {

		comment = new TextArea();

		comment.setPrefRowCount(7);
		comment.setPrefColumnCount(40);

		comment.caretPositionProperty().addListener(this::changeCaret);

		return createTitledPane(comment, "Comment");

	}

	private Pane createPoster() {

		VBox root = new VBox();

		//set default file path
		filePath = new Label(FX_ICON + "2 Fast 2 Furious.jpg");
		filePath.setMaxWidth(400);

		//create stage to open file chooser
		fileStage = new Stage();
		fileChooser = new FileChooser();

		//create button
		chooseFile = new Button("Choose File");
		chooseFile.setOnAction(actionHandler);

		//create image
		image = new Image(filePath.getText(), W, H, false, true);
		view = new ImageView();
		view.setImage(image);

		//add to box and return
		root.getChildren().add(view);
		root.getChildren().add(filePath);
		root.getChildren().add(chooseFile);

		return root;


	}
	// TODO #2a: In your methods, include code to register each widget with the
	// appropriate event listeners and/or property change handlers. Refer to the
	// javafx.scene.control package in the JavaFX APIs to learn about the events
	// and properties utilized by each widget type.

	//**********************************************************************
	// Private Methods (Property Change Handlers)
	//**********************************************************************
	
	// TODO #2b: Add any additional methods needed to register change listening
	// for the properties of the widgets you created for your layout.
	
	// TODO #9a: In the methods you added, implement code to pass the modified
	// value of the observed property to the corresponding data attribute value
	// in the model.

	private void	changeItem(ObservableValue<? extends String> observable,
							   String oldValue, String newValue)
	{
		if (observable == rating.getSelectionModel().selectedItemProperty())
			controller.set("rating", newValue);
	}

	private void	changeDecimal(ObservableValue<? extends Number> observable,
								  Number oldValue, Number newValue)
	{
		if (observable == slider.valueProperty())
			controller.set("myDouble", newValue);
		else if(observable == score.valueProperty())
			controller.set("score", newValue);
	}

	private void	changeInteger(ObservableValue<? extends Number> observable,
								  Number oldValue, Number newValue)
	{
		if (observable == spinner.valueProperty())
			controller.set("myInt", newValue);
		else if(observable == reviews.valueProperty())
			controller.set("reviews", newValue);
		else if(observable == year.valueProperty())
			controller.set("year", newValue);
	}

	//changes runtime
	private void	changeRuntime(ObservableValue<? extends Number> observable,
								  Number oldValue, Number newValue)
	{
		if (observable == runtime.valueProperty())
			controller.set("runtime", newValue);
	}

	//change caret
	private void	changeCaret(ObservableValue<? extends Number> observable,
								Number oldValue, Number newValue)
	{

		if (observable == summary.caretPositionProperty())
			controller.set("summary", summary.getText());
		else if(observable == comment.caretPositionProperty())
			controller.set("comment", comment.getText());
	}

	//**********************************************************************
	// Inner Classes (Event Handlers)
	//**********************************************************************
	
	// TODO #2c: Add any additional inner classes needed to register event
	// handling for the widgets you created for your layout.
	
	// TODO #9b: In the classes you added, implement the event handling method
	// to get the modified information in the relevant widget and use it to
	// update the corresponding data attribute value in the model.


	private final class ActionHandler
		implements EventHandler<ActionEvent>
	{
		public void	handle(ActionEvent e)
		{
			Object	source = e.getSource();

			if (source == textField)
				controller.set("myString", textField.getText());

			//handles the change in title
			else if(e.getSource() == title)
				controller.set("title", title.getText());

			//director
			else if(e.getSource() == director)
				controller.set("director", director.getText());

			//animated
			else if(e.getSource() == animated)
				controller.set("animated", animated.isSelected());

			//color
			else if(e.getSource() == color)
				controller.set("color", color.isSelected());

			//genres
			else if(e.getSource() == action)
				controller.set("action", action.isSelected());
			else if(e.getSource() == comedy)
				controller.set("comedy", comedy.isSelected());
			else if(e.getSource() == documentary)
				controller.set("documentary", documentary.isSelected());
			else if(e.getSource() == drama)
				controller.set("drama", drama.isSelected());
			else if(e.getSource() == fantasy)
				controller.set("fantasy", fantasy.isSelected());
			else if(e.getSource() == horror)
				controller.set("horror", horror.isSelected());
			else if(e.getSource() == romance)
				controller.set("romance", romance.isSelected());
			else if(e.getSource() == scifi)
				controller.set("scifi", scifi.isSelected());
			else if(e.getSource() == thriller)
				controller.set("thriller", thriller.isSelected());
			else if(e.getSource() == western)
				controller.set("western", western.isSelected());

			//awards
			else if(e.getSource() == picture)
				controller.set("picture", picture.isSelected());
			else if(e.getSource() == directing)
				controller.set("directing", directing.isSelected());
			else if(e.getSource() == cinema)
				controller.set("cinema", cinema.isSelected());
			else if(e.getSource() == acting)
				controller.set("acting", acting.isSelected());

			//poster
			else if(e.getSource() == chooseFile) {

				//open file chooser stage
				File file = fileChooser.showOpenDialog(fileStage);

				//if file is chosen, set the path and create new image to add into box
				if(file != null) {
					filePath.setText(file.getAbsolutePath());
					image = new Image(filePath.getText(), W, H, false, true);
					view = new ImageView();
					view.setImage(image);
				}

			}
		}


	}


}

//******************************************************************************
