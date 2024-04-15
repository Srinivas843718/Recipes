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
import {
  Visibility as VisibilityIcon,
  ThumbUp as ThumbUpIcon,
  ThumbDown as ThumbDownIcon,
  ChatBubbleOutline as ChatBubbleOutlineIcon,
} from "@mui/icons-material";
import { useNavigate } from "react-router-dom";
import api from "../../api/api";
import { useAuth } from "../../contexts/AuthContext";
import CustomSnackbar from "../home/CustomSnackbar";

const AdminRecipesList = () => {
  const navigate = useNavigate();
  const [recipes, setRecipes] = useState([]);
  const { user } = useAuth();

  const [snackbar, setSnackbar] = useState({
    message: "",
    alertType: "",
  });
  const [openSnackbar, setOpenSnackbar] = useState(false);
  const fetchRecipes = async () => {
    try {
      const response = await api.get(`/recipes`);
      setRecipes(response.data);
    } catch (error) {
      console.error("Error fetching recipes:", error);
    }
  };

  useEffect(() => {
    fetchRecipes();
  }, []);

  const handleViewRecipe = (recipeId) => {
    navigate(`/admin/recipes/${recipeId}/details`);
  };

  const handleRecipeStatus = async (recipeId, status) => {
    try {
      await api.put(`/recipes/${recipeId}/status-approval`, {
        status,
      });

      setSnackbar({
        message: "Recipe " + status,
        alertType: "success",
      });
      setOpenSnackbar(true);
      fetchRecipes();
    } catch (err) {
      setSnackbar({ message: err?.response.data, alertType: "error" });
      setOpenSnackbar(true);
      console.error(err);
      return "fail";
    }
  };

  return (
    <Box>
      <Grid container justifyContent="space-between" alignItems="center">
        <Grid item>
          <Typography variant="h4" gutterBottom>
            Recipes Pending Approval
          </Typography>
        </Grid>
      </Grid>

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

      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>
                <b>Title</b>
              </TableCell>
              <TableCell>
                <b>User name</b>
              </TableCell>
              <TableCell>
                <b>Likes</b>
              </TableCell>
              <TableCell>
                <b>Views</b>
              </TableCell>
              <TableCell>
                <b>Rating</b>
              </TableCell>
              <TableCell>
                <b>Subscription</b>
              </TableCell>
              <TableCell>
                <b>Status</b>
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
                <TableCell>{recipe.userName}</TableCell>
                <TableCell>{recipe.likes?.length || 0}</TableCell>
                <TableCell>{recipe.numberOfViews}</TableCell>
                <TableCell>{recipe.rating}</TableCell>
                <TableCell>{recipe.premium}</TableCell>
                <TableCell>{recipe.status}</TableCell>
                <TableCell>
                  <Box display={"flex"} flexWrap={"wrap"} flexDirection={"row"}>
                    <Button
                      variant="outlined"
                      color="primary"
                      startIcon={<VisibilityIcon />}
                      onClick={() => handleViewRecipe(recipe.recipeId)}
                      size="medium"
                      style={{ marginTop: 4 }}
                    >
                      View
                    </Button>
                    <Button
                      variant="outlined"
                      color="primary"
                      size="medium"
                      startIcon={<ChatBubbleOutlineIcon />}
                      onClick={() =>
                        navigate(`/admin/recipes/${recipe.recipeId}/comments`)
                      }
                      sx={{ marginLeft: 2 }}
                      style={{ marginTop: 4 }}
                    >
                      Comments
                    </Button>
                    <Button
                      variant="outlined"
                      color="primary"
                      size="medium"
                      startIcon={<ThumbUpIcon />}
                      onClick={() =>
                        handleRecipeStatus(recipe.recipeId, "Approved")
                      }
                      sx={{ marginLeft: 2 }}
                      style={{ marginTop: 4 }}
                    >
                      Approve
                    </Button>
                    <Button
                      variant="outlined"
                      color="primary"
                      size="medium"
                      startIcon={<ThumbDownIcon />}
                      onClick={() =>
                        handleRecipeStatus(recipe.recipeId, "Rejected")
                      }
                      sx={{ marginLeft: 2 }}
                      style={{ marginTop: 4 }}
                    >
                      Reject
                    </Button>
                  </Box>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </Box>
  );
};

export default AdminRecipesList;
