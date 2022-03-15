//******************************************************************************
// Copyright (C) 2020 University of Oklahoma Board of Trustees.
//******************************************************************************
// Last modified: Wed Feb 12 23:13:57 2020 by Chris Weaver
//******************************************************************************
// Major Modification History:
//
// 20200212 [weaver]:	Original file.
//
//******************************************************************************
//
//******************************************************************************

package edu.ou.cs.hci.assignment.prototypeb;

//import java.lang.*;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.*;
import javafx.scene.image.Image;

//******************************************************************************

/**
 * The <CODE>Movie</CODE> class manages the attributes of a movie as a set of
 * properties. The properties are created in the constructor. This differs from
 * the lazy creation of properties described in the TableView API (in the Person
 * class example), because we also use the properties to store the results of
 * parsing the inputs when the application starts.
 *
 * @author  Chris Weaver
 * @version %I%, %G%
 */
public final class Movie
{
	//**********************************************************************
	// Private Members
	//**********************************************************************

	// Each attribute has a matching property of the corresponding type.

	// TODO #0: Add members for the other 15 attributes.

	private final SimpleStringProperty		title;
	private final SimpleStringProperty		image;
	private final SimpleIntegerProperty		year;
	private final SimpleStringProperty		rating;
	private final SimpleIntegerProperty		runtime;

	private final SimpleBooleanProperty 	awardPicture;
	private final SimpleBooleanProperty		awardDirecting;
	private final SimpleBooleanProperty		awardCinematography;
	private final SimpleBooleanProperty		awardActing;

	private final SimpleDoubleProperty		reviewScore;
	private final SimpleIntegerProperty		numReviews;
	private final SimpleIntegerProperty 	genres;

	private final SimpleStringProperty		director;
	private final SimpleBooleanProperty		isAnimated;
	private final SimpleBooleanProperty		isColor;
	private final SimpleStringProperty		summary;
	private final SimpleStringProperty		comments;

	//**********************************************************************
	// Constructors and Finalizer
	//**********************************************************************

	public Movie(List<String> item)
	{
		// TODO #1: Create properties for the other 15 attributes.

		//get every attribute from item
		title = new SimpleStringProperty(item.get(0));
		image = new SimpleStringProperty(item.get(1));
		year = new SimpleIntegerProperty(Integer.parseInt(item.get(2)));
		//read rating as a string as it is a string in the csv file.
		rating = new SimpleStringProperty(item.get(3));
		runtime = new SimpleIntegerProperty(Integer.parseInt(item.get(4)));

		awardActing = new SimpleBooleanProperty(Boolean.parseBoolean(item.get(5)));
		awardDirecting = new SimpleBooleanProperty(Boolean.parseBoolean(item.get(6)));
		awardCinematography = new SimpleBooleanProperty(Boolean.parseBoolean(item.get(7)));
		awardPicture = new SimpleBooleanProperty(Boolean.parseBoolean(item.get(8)));

		reviewScore = new SimpleDoubleProperty(Double.parseDouble(item.get(9)));
		numReviews = new SimpleIntegerProperty(Integer.parseInt(item.get(10)));
		genres = new SimpleIntegerProperty(Integer.parseInt(item.get(11)));

		director = new SimpleStringProperty(item.get(12));
		isAnimated = new SimpleBooleanProperty(Boolean.parseBoolean(item.get(13)));
		isColor = new SimpleBooleanProperty(Boolean.parseBoolean(item.get(14)));
		summary	= new SimpleStringProperty(item.get(15));
		comments = new SimpleStringProperty(item.get(16));

	}

	//**********************************************************************
	// Public Methods (Getters and Setters)
	//**********************************************************************

	// Each attribute has methods to access and modify its value.

	// TODO #2: Create access and modify methods for the other 15 attributes.
	//Created get and set method for all the attributes

	//title
	public String	getTitle() {
		return title.get();
	}

	public void	setTitle(String v) {
		title.set(v);
	}

	//image
	public String	getImage() {
		return image.get();
	}

	public void	setImage(String v) {
		image.set(v);
	}

	//year
	public int	getYear() {
		return year.get();
	}

	public void setYear(int v) {
		year.set(v);
	}

	//rating
	public String	getRating() {
		return rating.get();
	}

	public void setRating(String v) {
		rating.set(v);
	}

	//runtime
	public int	getRuntime() {
		return runtime.get();
	}

	public void setRuntime(int v) {
		runtime.set(v);
	}

	//acting
	public boolean	getAwardActing() {
		return awardActing.get();
	}

	public void setAwardActing(boolean v) {
		awardActing.set(v);
	}

	//directing
	public boolean getAwardDirecting() {
		return awardDirecting.get();
	}

	public void setAwardDirecting(boolean v) {
		awardDirecting.set(v);
	}

	//cinematography
	public boolean getAwardCinematography() {
		return awardCinematography.get();
	}

	public void setAwardCinematography(boolean v) {
		awardCinematography.set(v);
	}

	//picture
	public boolean getAwardPicture () {
		return awardPicture.get();
	}

	public void setAwardPicture (boolean v) {
		awardPicture.set(v);
	}

	//reviewScore
	public double getReviewScore() {
		return reviewScore.get();
	}

	public void setReviewScore(double v) {
		reviewScore.set(v);
	}

	//numReviews
	public int getNumReviews() {
		return numReviews.get();
	}

	public void setNumReviews(int v) {
		numReviews.set(v);
	}

	//genres
	public int getGenres() {
		return genres.get();
	}

	public void setGenres(int v) {
		genres.set(v);
	}

	//director
	public String getDirector() {
		return director.get();
	}

	public void setDirector(String v) {
		director.set(v);
	}

	//isAnimated
	public boolean getIsAnimated() {
		return isAnimated.get();
	}

	public void setIsAnimated(boolean v) {
		isAnimated.set(v);
	}

	//isColor
	public boolean getIsColor() {
		return isColor.get();
	}

	public void setIsColor(boolean v) {
		isColor.set(v);
	}

	//summary
	public String getSummary() {
		return summary.get();
	}

	public void setSummary(String v) {
		summary.set(v);
	}

	//comments
	public String getComments() {
		return comments.get();
	}

	public void setComments(String v) {
		comments.set(v);
	}

	//**********************************************************************
	// Public Methods (Alternative Access Methods)
	//**********************************************************************

	// Convenience method for loading and resizing movie poster images. Loads
	// the image in the file named by the image property value, relative to the
	// given path, and returns a version resized to the given width and height.
	public Image	getImageAsImage(String path, double width, double height)
	{
		try
		{
			return new Image(path + getImage(), width, height, false, true);
		}
		catch (Exception ex)
		{
			return null;
		}
	}
}

//******************************************************************************
