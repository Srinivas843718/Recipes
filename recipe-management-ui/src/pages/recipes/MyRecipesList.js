import React, { useState, useEffect } from "react";
import {
  Box,
  Button,
  Grid,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  Typography,
} from "@mui/material";
import { Add as AddIcon, Edit as EditIcon } from "@mui/icons-material";
import { useNavigate } from "react-router-dom";
import api from "../../api/api";
import { useAuth } from "../../contexts/AuthContext";

const MyRecipesList = () => {
  const navigate = useNavigate();
  const [recipes, setRecipes] = useState([]);
  const { user } = useAuth();

  useEffect(() => {
    const fetchRecipes = async () => {
      try {
        const response = await api.get(`/users/${user?.userId}/my-recipes`);
        setRecipes(response.data);
      } catch (error) {
        console.error("Error fetching recipes:", error);
      }
    };

    fetchRecipes();
  }, []);

  const handleAddRecipe = () => {
    navigate("/user/my-recipes/create-new");
  };

  const handleEditRecipe = (recipeId) => {
    navigate(`/user/my-recipes/${recipeId}/update-recipe`);
  };

  return (
    <Box>
      <Grid container justifyContent="space-between" alignItems="center">
        <Grid item>
          <Typography variant="h4" gutterBottom>
            My Recipes
          </Typography>
        </Grid>
        <Grid item>
          <Button
            variant="contained"
            color="primary"
            startIcon={<AddIcon />}
            onClick={handleAddRecipe}
          >
            Add Recipe
          </Button>
        </Grid>
      </Grid>

      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>
                <b>Title</b>
              </TableCell>
              <TableCell>
                <b>Preparation Time</b>
              </TableCell>
              <TableCell>
                <b>Likes</b>
              </TableCell>
              <TableCell>
                <b>Rating</b>
              </TableCell>
              <TableCell>
                <b>Approval Status</b>
              </TableCell>
              <TableCell>
                <b>Subscription</b>
              </TableCell>
              <TableCell>
                <b>Actions</b>
              </TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {recipes.map((recipe) => (
              <TableRow key={recipe.recipeId}>
                <TableCell>{recipe.title}</TableCell>
                <TableCell>{recipe.preparationTime}</TableCell>
                <TableCell>{recipe.likes?.length || 0}</TableCell>
                <TableCell>{recipe.rating}</TableCell>
                <TableCell>{recipe.status}</TableCell>
                <TableCell>{recipe.premium}</TableCell>
                <TableCell>
                  <Button
                    variant="outlined"
                    color="primary"
                    onClick={() => {
                      navigate(`/user/my-recipes/${recipe.recipeId}/details`);
                    }}
                  >
                    View
                  </Button>
                  <Button
                    variant="outlined"
                    color="primary"
                    startIcon={<EditIcon />}
                    onClick={() => handleEditRecipe(recipe.recipeId)}
                    sx={{ marginLeft: 2 }}
                  >
                    Edit
                  </Button>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </Box>
  );
};

export default MyRecipesList;
