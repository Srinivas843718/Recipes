import React, { useState, useEffect } from "react";
import {
  Container,
  Typography,
  Grid,
  List,
  ListItem,
  ListItemText,
  Button,
  TextField,
  IconButton,
  Rating,
} from "@mui/material";
import { ThumbUp as ThumbUpIcon } from "@mui/icons-material";
import api from "../../api/api";
import { useNavigate, useParams } from "react-router-dom";
import { useAuth } from "../../contexts/AuthContext";
import CustomSnackbar from "../home/CustomSnackbar";

const RecipeDetails = () => {
  const { recipeId } = useParams();
  const [recipe, setRecipe] = useState(null);
  const [comments, setComments] = useState([]);
  const [newComment, setNewComment] = useState("");
  const [likeCount, setLikeCount] = useState(0);
  const [isLiked, setIsLiked] = useState(false);
  const [userRating, setUserRating] = useState(0); // Added state for user's rating
  const [rating, setRating] = useState(0);
  const navigate = useNavigate();
  const { user, isAuthenticated, isUser, isAdmin } = useAuth();
  const [snackbar, setSnackbar] = useState({
    message: "",
    alertType: "",
  });
  const [openSnackbar, setOpenSnackbar] = useState(false);

  const updateViews = async () => {
    try {
      await api.put(`/recipes/${recipeId}/views`, {});
    } catch (error) {
      console.error("Error updating views ", error);
    }
  };
  useEffect(() => {
    updateViews();
  }, []);

  useEffect(() => {
    const fetchRecipeAndSubscriptionDetails = async () => {
      try {
        const [recipeResponse, commentsResponse, subscriptionResponse] =
          await Promise.all([
            api.get(`/recipes/${recipeId}`),
            api.get(`/recipes/${recipeId}/comments`),
            api.get(`/subscriptions/user/${user?.userId}`),
          ]);
        setComments(commentsResponse.data);
        if (isAdmin()) {
          setRecipe(recipeResponse.data);
        }
        {
          if (
            recipeResponse.data?.premium === "Premium" &&
            !subscriptionResponse?.data
          ) {
            if (!isAuthenticated()) {
              setSnackbar({
                message: "You must login to view this recipe",
                alertType: "error",
              });
              setOpenSnackbar(true);
              setTimeout(() => {
                navigate("/user/login");
              }, 2000);
              return;
            } else if (isAuthenticated() && isUser()) {
              setSnackbar({
                message: "You must have subscription to view this recipe",
                alertType: "error",
              });
              setOpenSnackbar(true);
              setTimeout(() => {
                navigate("/user/subscription");
              }, 2000);
              return;
            }
          } else {
            setRecipe(recipeResponse.data);
          }
        }
      } catch (error) {
        console.error("Error fetching details:", error);
      }
    };

    fetchRecipeAndSubscriptionDetails();
  }, [recipeId, isAuthenticated, isUser, navigate]);

  useEffect(() => {
    if (recipe) {
      setLikeCount(!recipe.likes ? 0 : recipe.likes?.length);
      setRating(recipe.rating);
      setIsLiked(recipe.likes?.includes(user?.userId));
      const userRating = recipe.ratings?.find(
        (rating) => rating.userId === user?.userId
      );
      setUserRating(userRating?.rating);
    }
  }, [recipe]);

  const handleEditRecipe = () => {
    navigate(`/user/my-recipes/${recipeId}/update-recipe`);
  };

  const handleAddComment = async () => {
    if (!newComment) {
      setSnackbar({
        message: "Please enter a comment",
        alertType: "error",
      });
      setOpenSnackbar(true);

      return;
    }

    try {
      const response = await api.post("/comments", {
        recipeId,
        userId: user?.userId,
        comment: newComment,
        status: "Pending",
        userName:
          user && user.userId
            ? user.firstName + " " + user.lastName
            : "Anonymous",
      });

      setComments([...comments, response.data]);
      setNewComment("");
    } catch (error) {
      console.error("Error adding comment:", error);
    }
  };

  const handleLikeDislike = async () => {
    if (!isAuthenticated()) {
      setSnackbar({
        message: "Please login to like the recipe",
        alertType: "error",
      });
      setOpenSnackbar(true);
      return;
    }

    try {
      await api.put(`/recipes/${recipeId}/likes`, {
        userId: user?.userId,
        likes: [user?.userId],
      });

      setSnackbar({
        message: `Recipe ${isLiked ? "unliked" : "liked"}`,
        alertType: "success",
      });
      setOpenSnackbar(true);

      setLikeCount(isLiked ? likeCount - 1 : likeCount + 1);
      setIsLiked(!isLiked);
    } catch (error) {
      console.error("Error updating like/dislike:", error);
    }
  };

  const handleRateRecipe = async (newRating) => {
    if (!isAuthenticated()) {
      setSnackbar({
        message: "Please login to rate the recipe",
        alertType: "error",
      });
      setOpenSnackbar(true);
      return;
    }

    try {
      const res = await api.put(`/recipes/${recipeId}/rating`, {
        ratings: [{ userId: user?.userId, rating: newRating }],
      });

      setSnackbar({
        message: `Recipe rated ${newRating} stars`,
        alertType: "success",
      });
      setOpenSnackbar(true);

      setRating(res.data?.rating);
      const userRating = res.data?.ratings?.find(
        ({ userId }) => userId === user?.userId
      );
      setUserRating(userRating?.rating);
    } catch (error) {
      console.error("Error updating rating:", error);
    }
  };

  return (
    <Container
      maxWidth="md"
      sx={{
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        justifyContent: "center",
      }}
    >
      <CustomSnackbar
        open={openSnackbar}
        handleClose={() => {
          setSnackbar({
            message: "",
            alertType: "",
          });
          setOpenSnackbar(false);
        }}
        {...snackbar}
      />
      {recipe ? (
        <Grid container spacing={3}>
          {/* Left side: Recipe details */}
          <Grid item xs={12} sm={6}>
            <img
              src={
                recipe.imageUrl
                  ? require(`../../images/${recipe.imageUrl}`)
                  : ""
              }
              alt={recipe.title}
              style={{
                maxWidth: "100%",
                height: 300,
                width: "100%",
                objectFit: "cover",
              }}
            />
            <div style={{ width: "100%" }}>
              <Typography variant="h4" sx={{ marginTop: 2 }}>
                {recipe.title}
              </Typography>
              <Typography variant="subtitle1">
                Preparation Time: {recipe.preparationTime}
              </Typography>
              <Typography variant="h6">Ingredients:</Typography>
              <List>
                {recipe.ingredientsList.map((ingredient, index) => (
                  <ListItem key={index}>
                    <ListItemText
                      primary={`${ingredient.itemName}: ${ingredient.quantity} ${ingredient.measurement}`}
                    />
                  </ListItem>
                ))}
              </List>
              <Typography variant="h6">Instructions:</Typography>
              <ol>
                {recipe.instructions.map((instruction, index) => (
                  <li key={index}>{instruction}</li>
                ))}
              </ol>
              <Typography variant="h6">Nutrition Information:</Typography>
              <List>
                {recipe.nutritionsList.map((nutrition, index) => (
                  <ListItem key={index}>
                    <ListItemText
                      primary={`${nutrition.itemName}: ${nutrition.quantity}`}
                    />
                  </ListItem>
                ))}
              </List>
              {recipe.userId === user?.userId && (
                <Button
                  variant="contained"
                  color="primary"
                  onClick={handleEditRecipe}
                  sx={{ marginTop: 2 }}
                >
                  Edit Recipe
                </Button>
              )}
            </div>
          </Grid>

          {/* Right side: List of comments and Add Comment form */}
          <Grid item xs={12} sm={6}>
            <div style={{ width: "100%" }}>
              <div style={{ display: "flex", alignItems: "center" }}>
                {isAuthenticated() ? (
                  <div style={{ marginRight: "8px" }}>
                    <IconButton
                      color={isLiked ? "primary" : "default"}
                      onClick={handleLikeDislike}
                    >
                      <ThumbUpIcon />
                    </IconButton>
                  </div>
                ) : null}

                <Typography variant="body1" style={{ marginRight: "16px" }}>
                  {likeCount} Likes
                </Typography>
                <Typography variant="body1" style={{ marginRight: "16px" }}>
                  {recipe.numberOfViews} Views
                </Typography>
                <Typography variant="body1" style={{ marginRight: "16px" }}>
                  Subscription: {recipe.premium}
                </Typography>
              </div>

              <div
                style={{
                  display: "flex",
                  alignItems: "center",
                  marginTop: "16px",
                }}
              >
                {isAuthenticated() ? (
                  <div style={{ marginRight: "8px" }}>
                    <Typography variant="body1">Rate the Recipe:</Typography>
                    <Rating
                      name="recipe-rating"
                      value={userRating} // Display user's rating
                      precision={0.5}
                      onChange={(event, newRating) => {
                        handleRateRecipe(newRating);
                      }}
                      style={{ marginLeft: "8px" }}
                    />
                  </div>
                ) : null}

                {isAuthenticated() ? (
                  <Typography variant="body1" style={{ marginLeft: "16px" }}>
                    Total Rating: {rating?.toFixed(1)} Stars
                  </Typography>
                ) : (
                  <div style={{ marginRight: "8px" }}>
                    <Typography variant="body1">Total Rating:</Typography>
                    <Rating
                      name="recipe-rating"
                      value={rating} // Display user's rating
                      precision={0.5}
                      style={{ marginLeft: "8px" }}
                      readOnly={true}
                    />
                  </div>
                )}
              </div>

              <Typography variant="h6" sx={{ marginTop: 2 }}>
                Comments:
              </Typography>
              {comments && comments.length > 0 && (
                <List>
                  {comments
                    .filter(({ status }) => status === "Approved")
                    .map((comment, index) => (
                      <ListItem key={index}>
                        <Typography variant="body1">
                          {comment?.userName ? (
                            <span style={{ fontWeight: "bold" }}>
                              {comment?.userName}:
                            </span>
                          ) : (
                            <span style={{ fontStyle: "italic" }}>
                              Anonymous:
                            </span>
                          )}
                          <br />
                          {comment?.comment}
                        </Typography>
                      </ListItem>
                    ))}
                </List>
              )}
              {!isAdmin() && (
                <>
                  <TextField
                    label="Add a comment"
                    variant="outlined"
                    margin="normal"
                    fullWidth
                    value={newComment}
                    onChange={(e) => setNewComment(e.target.value)}
                  />
                  <Button
                    variant="contained"
                    color="primary"
                    onClick={handleAddComment}
                    sx={{ marginTop: 1 }}
                  >
                    Add Comment
                  </Button>
                </>
              )}
            </div>
          </Grid>
        </Grid>
      ) : (
        <p>Loading...</p>
      )}
    </Container>
  );
};

export default RecipeDetails;
