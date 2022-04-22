//******************************************************************************
// Copyright (C) 2020 University of Oklahoma Board of Trustees.
//******************************************************************************
// Last modified: Mon Apr 13 18:23:17 2020 by Chris Weaver
//******************************************************************************
// Major Modification History:
//
// 20200412 [weaver]:	Original file.
//
//******************************************************************************
//
//******************************************************************************

package edu.ou.cs.hci.assignment.prototypee.flow;

//import java.lang.*;
import java.util.List;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.effect.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import edu.ou.cs.hci.assignment.prototypee.Movie;

//******************************************************************************

/**
 * The <CODE>CoverItem</CODE> class.
 *
 * @author  Chris Weaver
 * @version %I%, %G%
 */
public final class CoverItem extends StackPane
{
	//**********************************************************************
	// Public Class Members (Resources)
	//**********************************************************************

	public static final String	RSRC		= "edu/ou/cs/hci/resources/";
	public static final String	FX_ICON	= RSRC + "example/fx/icon/";

	private static final double	W2 = 80;		// Item image width
	private static final double	H2 = W2 * 1.5;	// Item image height
	//**********************************************************************
	// Public Class Methods (Resources)
	//**********************************************************************

	// Convenience method to create a node for an image located in resources
	// relative to the FX_ICON package. See static member definitions above.
	public static ImageView	createFXIcon(String url, double w, double h)
	{
		Image	image = new Image(FX_ICON + url, w, h, false, true);

		return new ImageView(image);
	}

	//**********************************************************************
	// Private Class Members (Effects)
	//**********************************************************************

	private static final Font			FONT =
		Font.font("Serif", FontWeight.BOLD, 10.0);

	private static final ColorAdjust	COLOR_ADJUST =
		new ColorAdjust(-0.25, -0.25, -0.25, 0.0);

	private static final Glow			GLOW =
		new Glow(0.5);

	private static final Insets PADDING =
			new Insets(10.0, 5.0, 10.0, 5.0);
	//**********************************************************************
	// Private Members
	//**********************************************************************

	// Data
	private final List<String>		gdata;		// Genre strings
	private final List<String>		rdata;		// Rating strings

	// Members
	private Movie					movie;		// Movie represented
	private boolean				selected;	// Selected in the coverflow?

	// TODO #08: Add members for the elements used in your item layout.
	private TextField				label;
	private ImageView				image;
	private Circle 					action;
	private Circle 					comedy;
	private Circle 					documentary;
	private Circle 					drama;
	private Circle					fantasy;
	private Circle 					horror;
	private Circle 					romance;
	private Circle					scifi;
	private Circle 					thriller;
	private Circle 					western;
	private Circle					G;
	private Polygon					PG;
	private Rectangle				PG13;
	private Polygon					R;
	//**********************************************************************
	// Constructors and Finalizer
	//**********************************************************************

	public CoverItem(List<String> gdata, List<String> rdata)
	{
		// Get fixed data sets loaded by model from hardcoded file locations
		this.gdata = gdata;
		this.rdata = rdata;

		movie = null;
		selected = false;

		createLayout();

		// Use default layout and styling whenever the movie is null
		updateLayout();						// Build the layout
		updateStyles();						// Apply any styles
	}

	//**********************************************************************
	// Public Methods (Getters and Setters)
	//**********************************************************************

	public Movie	getMovie()
	{
		return movie;
	}

	public void	setMovie(Movie movie)
	{
		if (this.movie == movie)				// No change, do nothing
			return;

		if (this.movie != null)				// Unregister old movie
			unregisterPropertyListeners();

		this.movie = movie;					// Remember new movie

		updateLayout();						// Rebuild the layout
		updateStyles();						// Reapply any styles

		if (this.movie != null)				// Register new movie
			registerPropertyListeners();
	}

	public boolean	getSelected()
	{
		return selected;
	}

	public void	setSelected(boolean selected)
	{
		if (this.selected == selected)			// No change, do nothing
			return;

		this.selected = selected;				// Remember new selection state

		// In many designs, changing the selection only affects styles.
		// Comment out the next line if selection doesn't affect layout.
		updateLayout();						// Rebuild the layout
		updateStyles();						// Reapply any styles
	}

	//**********************************************************************
	// Private Methods (Layout)
	//**********************************************************************

	// TODO #09a: Create default layout and styling of the item.
	private void	createLayout()
	{
		// Create, style, and layout widgets in their initial state.
		// Focus on the aspects that don't change when the item is selected.
		label = new TextField();
		label.setFont(FONT);
		label.setAlignment(Pos.CENTER);
		label.setMaxWidth(80);
		image = new ImageView();

		//genres
		action = new Circle();
		action.setFill(Color.valueOf("A6CEE3"));

		comedy = new Circle();
		comedy.setFill(Color.valueOf("1F78B4"));

		documentary = new Circle();
		documentary.setFill(Color.valueOf("B2DF8A"));

		drama = new Circle();
		drama.setFill(Color.valueOf("33A02C"));

		fantasy = new Circle();
		fantasy.setFill(Color.valueOf("E31A1C"));

		horror = new Circle();
		horror.setFill(Color.valueOf("FB9A99"));

		romance = new Circle();
		romance.setFill(Color.valueOf("FDBF6F"));

		scifi = new Circle();
		scifi.setFill(Color.valueOf("FF7F00"));

		thriller = new Circle();
		thriller.setFill(Color.valueOf("CAB2D6"));

		western = new Circle();
		western.setFill(Color.valueOf("6A3D9A"));

		//circle
		G = new Circle();
		G.setFill(Color.WHITE);
		G.setStroke(Color.BLACK);

		//diamond
		PG = new Polygon();
		PG.setFill(Color.WHITE);
		PG.setStroke(Color.BLACK);

		//square
		PG13 = new Rectangle();
		PG13.setFill(Color.WHITE);
		PG13.setStroke(Color.BLACK);

		//triangle
		R = new Polygon();
		R.setFill(Color.WHITE);
		R.setStroke(Color.BLACK);

		//create Hbox of shapes
		HBox shapes = new HBox(action, comedy, documentary, drama, fantasy, horror, romance, scifi, thriller,
				western, G, PG, PG13, R);

		// You can use more levels of pane to allow application of extra effects
//		StackPane	pane = new StackPane(image, label);
//		pane.setAlignment(Pos.BOTTOM_CENTER);
//		pane.setEffect(new DropShadow());

		VBox pane = new VBox(image, label, shapes);
		pane.setAlignment(Pos.BOTTOM_CENTER);
		pane.setEffect(new DropShadow());
		getChildren().addAll(pane);
	}

	// TODO #09b: Update item layout when its movie is selected/unselected.
	private void	updateLayout()
	{
		if (movie == null)
		{
			label.setText("");
			image.setImage(null);
		}
		else
		{
			label.setText(movie.getTitle());
			image.setImage(movie.getImageAsImage(FX_ICON, W2, H2));
			String genre = movie.getGenreAsString(gdata);
				if(genre.contains("Action"))
					action.setRadius(5);
				if(genre.contains("Comedy"))
					comedy.setRadius(5);
				if(genre.contains("Documentary"))
					documentary.setRadius(5);
				if(genre.contains("Drama"))
					drama.setRadius(5);
				if(genre.contains("Fantasy"))
					fantasy.setRadius(5);
				if(genre.contains("Horror"))
					horror.setRadius(5);
				if(genre.contains("Romance"))
					romance.setRadius(5);
				if(genre.contains("Sci-Fi"))
					scifi.setRadius(5);
				if(genre.contains("Thriller"))
					thriller.setRadius(5);
				if(genre.contains("Western"))
					western.setRadius(5);

				String rating = movie.getRating();
			switch (rating) {
				case "G":
					G.setRadius(7);
					break;
				case "PG":
					PG.getPoints().addAll(5.0, 0.0, 0.0, 5.0, 5.00, 10.0, 10.0, 5.0);
					break;
				case "PG-13":
					PG13.setWidth(10);
					PG13.setHeight(10);
					break;
				case "R":
					R.getPoints().addAll(10.0, 0.0, 0.0, 10.0, 20.0, 10.0);
					break;
			}
		}
	}

	// TODO #09c: Update item styling when its movie is selected/unselected.
	private void	updateStyles()
	{
		if (selected)	// Make the item appear brighter
		{
			label.setStyle("-fx-text-inner-color: blue;");
			label.setEffect(GLOW);
		}
		else			// Make the item appear darker
		{
			label.setStyle("-fx-text-inner-color: black;");
			label.setEffect(null);
		}
	}

	//**********************************************************************
	// Private Methods (Property Management)
	//**********************************************************************

	// TODO #10a: Uncomment lines below for the movie properties you use.
	private void	registerPropertyListeners()
	{
		movie.titleProperty().addListener(this::handleChangeS);
		movie.imageProperty().addListener(this::handleChangeS);

		//movie.yearProperty().addListener(this::handleChangeI);
		movie.ratingProperty().addListener(this::handleChangeS);
		//movie.runtimeProperty().addListener(this::handleChangeI);

		//movie.awardPictureProperty().addListener(this::handleChangeB);
		//movie.awardDirectingProperty().addListener(this::handleChangeB);
		//movie.awardCinematographyProperty().addListener(this::handleChangeB);
		//movie.awardActingProperty().addListener(this::handleChangeB);

		movie.averageReviewScoreProperty().addListener(this::handleChangeD);
		//movie.numberOfReviewsProperty().addListener(this::handleChangeI);
		movie.genreProperty().addListener(this::handleChangeI);

		//movie.directorProperty().addListener(this::handleChangeS);
		movie.isAnimatedProperty().addListener(this::handleChangeB);
		//movie.isColorProperty().addListener(this::handleChangeB);

		//movie.summaryProperty().addListener(this::handleChangeS);
		//movie.commentsProperty().addListener(this::handleChangeS);
	}

	// TODO #10b: Uncomment lines below for the movie properties you use.
	private void	unregisterPropertyListeners()
	{
		movie.titleProperty().removeListener(this::handleChangeS);
		movie.imageProperty().removeListener(this::handleChangeS);

		//movie.yearProperty().removeListener(this::handleChangeI);
		movie.ratingProperty().removeListener(this::handleChangeS);
		//movie.runtimeProperty().removeListener(this::handleChangeI);

		//movie.awardPictureProperty().removeListener(this::handleChangeB);
		//movie.awardDirectingProperty().removeListener(this::handleChangeB);
		//movie.awardCinematographyProperty().removeListener(this::handleChangeB);
		//movie.awardActingProperty().removeListener(this::handleChangeB);

		movie.averageReviewScoreProperty().removeListener(this::handleChangeD);
		//movie.numberOfReviewsProperty().removeListener(this::handleChangeI);
		movie.genreProperty().removeListener(this::handleChangeI);

		//movie.directorProperty().removeListener(this::handleChangeS);
		movie.isAnimatedProperty().removeListener(this::handleChangeB);
		//movie.isColorProperty().removeListener(this::handleChangeB);

		//movie.summaryProperty().removeListener(this::handleChangeS);
		//movie.commentsProperty().removeListener(this::handleChangeS);
	}

	//**********************************************************************
	// Private Methods (Property Change Handlers)
	//**********************************************************************

	private void	handleChangeS(ObservableValue<? extends String> observable,
								  String oldValue, String newValue)
	{
		updateLayout();
		updateStyles();
	}

	private void	handleChangeB(ObservableValue<? extends Boolean> observable,
								  Boolean oldValue, Boolean newValue)
	{
		updateLayout();
		updateStyles();
	}

	private void	handleChangeD(ObservableValue<? extends Number> observable,
								  Number oldValue, Number newValue)
	{
		updateLayout();
		updateStyles();
	}

	private void	handleChangeI(ObservableValue<? extends Number> observable,
								  Number oldValue, Number newValue)
	{
		updateLayout();
		updateStyles();
	}
}

//******************************************************************************
