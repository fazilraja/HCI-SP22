//******************************************************************************
// Copyright (C) 2019-2020 University of Oklahoma Board of Trustees.
//******************************************************************************
// Last modified: Fri Feb 14 12:15:51 2020 by Chris Weaver
//******************************************************************************
// Major Modification History:
//
// 20190203 [weaver]:	Original file.
// 20190220 [weaver]:	Adapted from swingmvc to fxmvc.
// 20200212 [weaver]:	Overhauled for new PrototypeB in Spring 2020.
//
//******************************************************************************
//
//******************************************************************************

package edu.ou.cs.hci.assignment.prototypeb.pane;

//import java.lang.*;
import java.util.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.*;
import edu.ou.cs.hci.assignment.prototypeb.*;
import edu.ou.cs.hci.resources.Resources;

//******************************************************************************

/**
 * The <CODE>CollectionPane</CODE> class.
 *
 * @author  Chris Weaver
 * @version %I%, %G%
 */
public final class CollectionPane extends AbstractPane
{
	//**********************************************************************
	// Private Class Members
	//**********************************************************************

	private static final String	NAME = "Collection";
	private static final String	HINT = "Movie Collection Browser";

	//**********************************************************************
	// Private Class Members (Layout)
	//**********************************************************************

	private static final double	W = 38;		// Item icon width
	private static final double	H = W * 1.5;	// Item icon height

	//measurements for summary Image
	private static final double WIDTH = 180;
	private static final double HEIGHT = WIDTH * 1.5;

	private static final Insets	PADDING =
		new Insets(40.0, 20.0, 40.0, 20.0);

	//**********************************************************************
	// Private Members
	//**********************************************************************

	// Data
	private final List<String>			gdata;		// Genre strings
	private final List<String>			rdata;		// Rating strings
	private final List<List<String>>	mdata;		// Movie attributes

	// Collection
	private final List<Movie>			movies;	// Movie objects

	// Layout
	private TableView<Movie>			table;
	private SelectionModel<Movie>		smodel;

	// Part of TODO #9: Add members for the widgets in your summary design here.
	private Label						summaryTitle;
	private ImageView					summaryImage;
	//create the 3 attributes as labels so they are not editable
	private Label 						summaryYear;
	private Label 						summaryRating;
	private Label 						summaryRuntime;

	//**********************************************************************
	// Constructors and Finalizer
	//**********************************************************************

	public CollectionPane(Controller controller)
	{
		super(controller, NAME, HINT);

		// Load data sets from hardcoded file locations
		gdata = Resources.getLines("data/genres.txt");
		rdata = Resources.getLines("data/ratings.txt");
		mdata = Resources.getCSVData("data/movies.csv");

		// Convert the raw movie data into movie objects
		movies = new ArrayList<Movie>();

		for (List<String> item : mdata)
			movies.add(new Movie(item));

		// Construct the pane
		setBase(buildPane());
	}

	//**********************************************************************
	// Public Methods (Controller)
	//**********************************************************************

	// The controller calls this method when it adds a view.
	// Set up the nodes in the view with data accessed through the controller.
	public void	initialize()
	{
		smodel.selectedIndexProperty().addListener(this::changeIndex);

		int	index = (Integer)controller.get("selectedMovieIndex");

		smodel.select(index);

		// Part of TODO #9: Initialize your summary widgets here, to show the
		// movie's attributes (if one is selected) or default values (if not).

		//set movies at the first movie in list
		//get the title and set the label
		summaryTitle.setText(movies.get(0).getTitle());

		//image for summary
		summaryImage.setImage(movies.get(0).getImageAsImage(FX_ICON, WIDTH, HEIGHT));

		//since label is string get the year as string
		summaryYear.setText(String.valueOf(movies.get(0).getYear()));

		//rating label
		summaryRating.setText(String.valueOf(movies.get(0).getRating()));

		//runtime label
		//setting up time to display runtime as hour and minutes
		int time = movies.get(0).getRuntime();
		int hour = time / 60;
		int minutes = (time - hour * 60) % 60;
		summaryRuntime.setText(hour + "h" + " " + minutes + "m");

	}

	// The controller calls this method when it removes a view.
	// Unregister event and property listeners for the nodes in the view.
	public void	terminate()
	{
		smodel.selectedIndexProperty().removeListener(this::changeIndex);

		// Part of TODO #9: Terminate your summary widgets here, as needed.
		//set everything to null
		summaryTitle.setText(null);
		summaryImage.setImage(null);
		summaryYear.setText(null);
		summaryRating.setText(null);
		summaryRuntime.setText(null);
	}

	// The controller calls this method whenever something changes in the model.
	// Update the nodes in the view to reflect the change.
	public void	update(String key, Object value)
	{
		if ("selectedMovieIndex".equals(key))
		{
			int	index = (Integer)value;
			Movie	movie = movies.get(index);

			smodel.select(index);

			// Part of TODO #9: Update your summary widgets here, to show the
			// movie attributes (if one is selected) or default values (if not).
			//set the attributes
			summaryTitle.setText(movie.getTitle());
			summaryImage.setImage(movie.getImageAsImage(FX_ICON, WIDTH, HEIGHT));
			summaryYear.setText(String.valueOf(movie.getYear()));
			summaryRating.setText(movie.getRating());

			//for runtime, change to hour and minutes
			int time = movie.getRuntime();
			int hour = time / 60;
			int minutes = (time - hour * 60) % 60;
			summaryRuntime.setText(hour + "h" + " " + minutes + "m");
		}
	}

	//**********************************************************************
	// Private Methods (Layout)
	//**********************************************************************

	private Pane	buildPane()
	{
		Node	bregion = buildTableView();
		Node	tregion = buildCoverFlow();
		Node	lregion = buildLaterView();
		Node	rregion = buildMovieView();

		// Create a split pane to share space between the cover pane and table
		SplitPane	splitPane = new SplitPane();

		splitPane.setOrientation(Orientation.VERTICAL);
		splitPane.setDividerPosition(0, 0.1);	// Put divider at 50% T-to-B

		splitPane.getItems().add(tregion);		// Cover flow at the top...
		splitPane.getItems().add(bregion);		// ...table view at the bottom

		StackPane	lpane = new StackPane(lregion);
		StackPane	rpane = new StackPane(rregion);

		return new BorderPane(splitPane, null, rregion, null, lregion);
	}

	private TableView<Movie>	buildTableView()
	{
		// Create the table and grab its selection model
		table = new TableView<Movie>();
		smodel = table.getSelectionModel();

		// Set up some helpful stuff including single selection mode
		table.setEditable(true);
		table.setPlaceholder(new Text("No Data!"));
		table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		// Add columns for title and image
		table.getColumns().add(buildTitleColumn());
		table.getColumns().add(buildImageColumn());

		// TODO #8: Uncomment these to add columns for your three attributes.
		table.getColumns().add(buildYearColumn());
		table.getColumns().add(buildRatingColumn());
		table.getColumns().add(buildRuntimeColumn());

		// Put the movies into an ObservableList to use as the table model
		table.setItems(FXCollections.observableArrayList(movies));

		return table;
	}

	private Node	buildCoverFlow()
	{
		Label	label = new Label("this space reserved for cover flow (later)");

		label.setPadding(PADDING);

		return label;
	}

	private Node	buildLaterView()
	{
		Label	label = new Label("saving for later");

		label.setPadding(PADDING);

		return label;
	}

	// TODO #9: Build the layout for your movie summary here, showing the title,
	// image, and your three attributes. For any widgets you use, add members
	// and/or code to initialize(), terminate(), and update() above, as needed.
	// Keep in mind that the movie summary is meant for display, not editing.
	private Node	buildMovieView()
	{
		// The label is just a placeholder. Replace it with your own widgets!
		//Label	label = new Label("replace me now!");

		VBox movieView = new VBox();

		//title label
		summaryTitle = new Label();
		summaryTitle.setFont(Font.font(26));
		summaryTitle.setAlignment(Pos.CENTER);
		summaryTitle.setMaxWidth(250);
		summaryTitle.setMinWidth(250);
		summaryTitle.setPadding(new Insets(20, 0, 0, 0));

		//image label
		summaryImage = new ImageView();
		//put image in a hbox to set alignment
		HBox boxImage = new HBox();
		boxImage.getChildren().add(summaryImage);
		//make it align center
		boxImage.setAlignment(Pos.CENTER);
		//add padding to top to create space from title
		boxImage.setPadding(new Insets(10, 0, 0, 0));

		//year label
		summaryYear = new Label();
		summaryYear.setFont(Font.font(18));
		HBox boxYear = new HBox();
		boxYear.getChildren().add(summaryYear);
		//align center
		boxYear.setAlignment(Pos.CENTER);
		//add padding top from image
		boxYear.setPadding(new Insets(10, 0, 0, 0));

		//rating label
		summaryRating = new Label();
		summaryRating.setFont(Font.font(18));
		HBox boxRating = new HBox();
		boxRating.getChildren().add(summaryRating);
		boxRating.setAlignment(Pos.CENTER);//align to center
		boxRating.setPadding(new Insets(10, 0, 0, 0));//padding top to create space from year

		//runtime label
		summaryRuntime = new Label();
		summaryRuntime.setFont(Font.font(18));
		HBox boxRuntime = new HBox();
		boxRuntime.getChildren().add(summaryRuntime);
		boxRuntime.setAlignment(Pos.CENTER);
		boxRuntime.setPadding(new Insets(10, 0, 0, 0));//padding between rating and runtime

		//add all of them to a VBox
		movieView.getChildren().add(summaryTitle);
		movieView.getChildren().add(boxImage);
		movieView.getChildren().add(boxYear);
		movieView.getChildren().add(boxRating);
		movieView.getChildren().add(boxRuntime);

		//return movieView the VBox
		return movieView;
	}

	//**********************************************************************
	// Private Methods (Table Columns)
	//**********************************************************************

	// This TableColumn displays titles, and allows editing.
	private TableColumn<Movie, String>	buildTitleColumn()
	{
		TableColumn<Movie, String>	column =
			new TableColumn<Movie, String>("Title");

		column.setEditable(true);
		column.setPrefWidth(250);
		column.setCellValueFactory(
			new PropertyValueFactory<Movie, String>("title"));
		column.setCellFactory(new TitleCellFactory());

		// Edits in this column update movie titles
		column.setOnEditCommit(new TitleEditHandler());

		return column;
	}

	// This TableColumn displays images, and does not allow editing.
	private TableColumn<Movie, String>	buildImageColumn()
	{
		TableColumn<Movie, String>	column =
			new TableColumn<Movie, String>("Image");

		column.setEditable(false);
		column.setPrefWidth(W + 8.0);
		column.setCellValueFactory(
			new PropertyValueFactory<Movie, String>("image"));
		column.setCellFactory(new ImageCellFactory());

		return column;
	}

	// TODO #7: Complete the TableColumn methods for your three attributes.
	// You must adapt the code to the column's attribute type in each case.

	// This TableColumn displays the year, and allows editing.
	private TableColumn<Movie, Integer>	buildYearColumn()
	{
		TableColumn<Movie, Integer>	column =
				new TableColumn<Movie, Integer>("Year");

		column.setEditable(true);
		column.setPrefWidth(95);
		column.setCellValueFactory(
				new PropertyValueFactory<Movie, Integer>("year"));
		column.setCellFactory(new YearCellFactory());

		// Edits in this column update movie titles
		column.setOnEditCommit(new YearEditHandler());

		return column;
	}

	// This TableColumn displays titles, and allows editing.
	private TableColumn<Movie, String>	buildRatingColumn()
	{
		TableColumn<Movie, String>	column =
				new TableColumn<Movie, String>("Rating");

		column.setEditable(true);
		column.setPrefWidth(95);
		column.setCellValueFactory(
				new PropertyValueFactory<Movie, String>("rating"));
		column.setCellFactory(new RatingCellFactory());

		// Edits in this column update movie titles
		column.setOnEditCommit(new RatingEditHandler());

		return column;
	}

	// This TableColumn displays the runtime, and allows editing.
	private TableColumn<Movie, Integer>	buildRuntimeColumn()
	{
		TableColumn<Movie, Integer>	column =
				new TableColumn<Movie, Integer>("Runtime");

		column.setEditable(true);
		column.setPrefWidth(95);
		column.setCellValueFactory(
				new PropertyValueFactory<Movie, Integer>("runtime"));
		column.setCellFactory(new RuntimeCellFactory());

		// Edits in this column update movie titles
		column.setOnEditCommit(new RuntimeEditHandler());

		return column;
	}

	//**********************************************************************
	// Private Methods (Change Handlers)
	//**********************************************************************

	private void	changeIndex(ObservableValue<? extends Number> observable,
								Number oldValue, Number newValue)
	{
		int	index = (Integer)newValue;

		controller.set("selectedMovieIndex", index);
	}

	//**********************************************************************
	// Inner Classes (Cell Factories)
	//**********************************************************************

	// This CellFactory creates Cells for the title column in the table.
	private final class TitleCellFactory
		implements Callback<TableColumn<Movie, String>,
							TableCell<Movie, String>>
	{
		public TableCell<Movie, String>	call(TableColumn<Movie, String> v)
		{
			return new TitleCell();
		}
	}

	// This CellFactory creates Cells for the image column in the table.
	private final class ImageCellFactory
		implements Callback<TableColumn<Movie, String>,
							TableCell<Movie, String>>
	{
		public TableCell<Movie, String>	call(TableColumn<Movie, String> v)
		{
			return new ImageCell();
		}
	}

	// TODO #6: Complete the CellFactory classes for your three attributes.
	// You must adapt the code to the column's attribute type in each case.

	//This Cell Factory create Cells for the year column in the table
	 private final class YearCellFactory
		 implements Callback<TableColumn<Movie, Integer>,
			 				TableCell<Movie, Integer>>
	 {
		 public TableCell<Movie, Integer> call(TableColumn<Movie, Integer> v) {return new YearCell();}
	 }

	 //this cell factory creates cell for the rating column
	private final class RatingCellFactory
			implements Callback<TableColumn<Movie, String>,
								TableCell<Movie, String>>
	{
		public TableCell<Movie, String>	call(TableColumn<Movie, String> v)
		{
			return new RatingCell();
		}
	}

	//creates cells for runtime column in table
	private final class RuntimeCellFactory
			implements Callback<TableColumn<Movie, Integer>,
			TableCell<Movie, Integer>>
	{
		public TableCell<Movie, Integer> call(TableColumn<Movie, Integer> v) {return new RuntimeCell();}
	}


	//**********************************************************************
	// Inner Classes (Cells)
	//**********************************************************************

	// This TableCell displays the title, and allows editing in a TextField.
	private final class TitleCell
		extends TextFieldTableCell<Movie, String>
	{
		public TitleCell()
		{
			super(new DefaultStringConverter());	// Since values are Strings
		}

		public void	updateItem(String value, boolean isEmpty)
		{
			super.updateItem(value, isEmpty);		// Prepare for setup

			if (isEmpty || (value == null))		// Handle special cases
			{
				setText(null);
				setGraphic(null);

				return;
			}

			// This cell shows the value of the title attribute as simple text.
			// If the title is too long, an ellipsis is inserted in the middle.
			String	title = value;

			setText(title);
			setTextOverrun(OverrunStyle.CENTER_ELLIPSIS);
			setGraphic(null);
		}
	}

	// This TableCell displays the image, and doesn't allow editing.
	private final class ImageCell
		extends TableCell<Movie, String>
	{
		public void	updateItem(String value, boolean isEmpty)
		{
			super.updateItem(value, isEmpty);		// Prepare for setup

			if (isEmpty || (value == null))		// Handle special cases
			{
				setText(null);
				setGraphic(null);
				setAlignment(Pos.CENTER);

				return;
			}

			// This cell uses the value of the posterFileName attribute
			// to show an image loaded from resources/example/fx/icon.
			String		posterFileName = value;
			ImageView	image = createFXIcon(posterFileName, W, H);

			setText(null);
			setGraphic(image);
			setAlignment(Pos.CENTER);
		}
	}

	// TODO #5: Complete the Cell classes for your three attributes.
	// You must adapt the code to the column's attribute type in each case.
	// Allow editing (shallowly) in at least one of the three columns.

	//for year cell
	private final class YearCell
			extends TextFieldTableCell<Movie, Integer>
	{
		//super method for string converter from integer
		public YearCell()
		{
			super(new StringConverter<Integer>() {
				@Override
				public String toString(Integer object) {
					return null;
				}

				@Override
				public Integer fromString(String string) {
					return null;
				}
			});	// Since values are Integer
		}
		public void	updateItem(Integer value, boolean isEmpty)
		{
			super.updateItem(value, isEmpty);		// Prepare for setup

			if (isEmpty)		// Handle special cases
			{
				setText(null);
				setGraphic(null);
				return;
			}

			setText(String.valueOf(value));
			setGraphic(null);
		}
	}

	//for rating cell
	private final class RatingCell
			extends TextFieldTableCell<Movie, String>
	{
		public RatingCell()
		{
			super(new DefaultStringConverter());	// Since values are Strings
		}

		public void	updateItem(String value, boolean isEmpty)
		{
			super.updateItem(value, isEmpty);		// Prepare for setup

			if (isEmpty || (value == null))		// Handle special cases
			{
				setText(null);
				setGraphic(null);

				return;
			}

			// This cell shows the value of the rating attribute as simple text.
			// If the rating is too long, an ellipsis is inserted in the middle.
			String	rating = value;

			setText(rating);
			setTextOverrun(OverrunStyle.CENTER_ELLIPSIS);
			setGraphic(null);
		}
	}

	//cell class for runtime cell
	private final class RuntimeCell
			extends TextFieldTableCell<Movie, Integer>
	{
		//string converter from integer
		public RuntimeCell()
		{
			super(new StringConverter<Integer>() {
				@Override
				public String toString(Integer object) {
					return null;
				}

				@Override
				public Integer fromString(String string) {
					return null;
				}
			});	// Since values are Integer
		}
		public void	updateItem(Integer value, boolean isEmpty)
		{
			super.updateItem(value, isEmpty);		// Prepare for setup

			if (isEmpty || (value == null))		// Handle special cases
			{
				setText(null);
				setGraphic(null);

				return;
			}

			//display as hour and minutes
			int time = value;
			int hour = time / 60;
			int minutes = (time - hour * 60) % 60;
			setText(hour + "h" + " " + minutes + "m");
			setGraphic(null);
		}
	}

	//**********************************************************************
	// Inner Classes (Table Column Edit Handlers)
	//**********************************************************************

	// This EventHander processes edits in the title column.
	private final class TitleEditHandler
		implements EventHandler<TableColumn.CellEditEvent<Movie, String>>
	{
		public void	handle(TableColumn.CellEditEvent<Movie, String> t)
		{
			// Get the movie for the row that was edited
			int	index = t.getTablePosition().getRow();
			Movie	movie = movies.get(index);

			// Set its title to the new value that was entered
			movie.setTitle(t.getNewValue());
		}
	}

	// TODO #4: Add an EventHandler class for each of your editable columns.
	// You must adapt the code to the column's attribute type in each case.
	// Allow editing (shallowly) in at least one of the three columns.

	//event handler to change the year, of type Integer
	 private final class YearEditHandler
		 implements EventHandler<TableColumn.CellEditEvent<Movie, Integer>>
	 {
		 public void handle(TableColumn.CellEditEvent<Movie, Integer> t)
		 {
			 //get the movie for the row that was edited
			 int index = t.getTablePosition().getRow();
			 Movie movie = movies.get(index);

			 //set its year to the new year that was entered
			 movie.setYear(t.getNewValue());
		 }
	 }

	 //even handler for rating
	 private final class RatingEditHandler
			 implements EventHandler<TableColumn.CellEditEvent<Movie, String>>
	 {
		 public void	handle(TableColumn.CellEditEvent<Movie, String> t)
		 {
			 // Get the movie for the row that was edited
			 int	index = t.getTablePosition().getRow();
			 Movie	movie = movies.get(index);

			 // Set its rating to the new value that was entered
			 movie.setRating(t.getNewValue());
		 }
	 }

	 //event handler for runtime
	private final class RuntimeEditHandler
			implements EventHandler<TableColumn.CellEditEvent<Movie, Integer>>
	{
		public void handle(TableColumn.CellEditEvent<Movie, Integer> t)
		{
			//get the movie for the row that was edited
			int index = t.getTablePosition().getRow();
			Movie movie = movies.get(index);

			//set its runtime to the new runtime that was entered
			//but as hour and minutes
			movie.setRuntime(t.getNewValue());
		}
	}
}

//******************************************************************************
