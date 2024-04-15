import React, { useState, useEffect } from "react";
import {
  Box,
  Card,
  CardActionArea,
  CardContent,
  CardMedia,
  Grid,
  Typography,
  Button,
  TextField,
} from "@mui/material";
import { useNavigate } from "react-router-dom";
import api from "../../api/api"; // Import your API module

const RecipesList = () => {
  const navigate = useNavigate();
  const [recipes, setRecipes] = useState([]);
  const [filteredRecipes, setFilteredRecipes] = useState([]);
  const [filter, setFilter] = useState("");

  useEffect(() => {
    const fetchRecipes = async () => {
      try {
        const response = await api.get("/recipes");
        setRecipes(response.data);
        setFilteredRecipes(response.data);
      } catch (error) {
        console.error("Error fetching recipes:", error);
      }
    };

    fetchRecipes();
  }, []);

  useEffect(() => {
    // Filter recipes based on the entered value
    const filtered = recipes.filter((recipe) => {
      const searchString = filter.toLowerCase();
      const titleMatch =
        recipe.title && recipe.title.toLowerCase().includes(searchString);
      const timeMatch =
        recipe.preparationTime &&
        recipe.preparationTime.toLowerCase().includes(searchString);

      const ingredientsMatch =
        recipe.ingredientsList &&
        recipe.ingredientsList.some((ingredient) =>
          ingredient.itemName.toLowerCase().includes(searchString)
        );

      const nutritionsMatch =
        recipe.nutritionsList &&
        recipe.nutritionsList.some((nutrition) =>
          nutrition.itemName.toLowerCase().includes(searchString)
        );

      return titleMatch || timeMatch || ingredientsMatch || nutritionsMatch;
    });

    setFilteredRecipes(filtered);
  }, [filter, recipes]);

  const handleRecipeDetails = (recipeId) => {
    navigate(`/recipes/${recipeId}/details`);
  };

  const handleFilterChange = (event) => {
    setFilter(event.target.value);
  };

  return (
    <Box padding={1}>
      <Box mb={2}>
        <TextField
          label="Filter Recipes"
          value={filter}
          onChange={handleFilterChange}
          variant="outlined"
          size="small"
        />
      </Box>
      {!filteredRecipes || filteredRecipes.length === 0 ? (
        <Typography variant="h6" gutterBottom sx={{ marginTop: 4 }}>
          {filter ? "No Recipes found ...." : ""}
        </Typography>
      ) : (
        <Grid container spacing={3}>
          {filteredRecipes
            .filter(({ status }) => status === "Approved")
            .map((recipe) => (
              <Grid item key={recipe.id} xs={12} sm={6} md={4} lg={3}>
                <Card
                  style={{ cursor: "pointer" }}
                  onClick={() => handleRecipeDetails(recipe.recipeId)}
                >
                  <CardActionArea>
                    <CardMedia
                      component="img"
                      height="160"
                      image={
                        recipe.imageUrl
                          ? require(`../../images/${recipe.imageUrl}`)
                          : ""
                      }
                      alt={recipe.title}
                    />

                    <CardContent>
                      <Typography variant="h6" component="div">
                        {recipe.title}
                      </Typography>
                      <Typography
                        variant="subtitle1"
                        color={"gray"}
                        component="div"
                      >
                        {recipe.premium}
                      </Typography>
                    </CardContent>
                  </CardActionArea>
                  <Box display="flex" justifyContent="center" padding={2}>
                    <Button
                      variant="outlined"
                      color="primary"
                      onClick={() => handleRecipeDetails(recipe.recipeId)}
                    >
                      View Recipe
                    </Button>
                  </Box>
                </Card>
              </Grid>
            ))}
        </Grid>
      )}
    </Box>
  );
};

export default RecipesList;
