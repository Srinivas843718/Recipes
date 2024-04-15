// UpdateRecipe.js
import React, { useState, useEffect } from "react";
import { Box, Button, TextField, Typography, Checkbox } from "@mui/material";
import ItemQuantityModal from "./ItemQuantityModal";
import CustomSnackbar from "../home/CustomSnackbar";
import { useAuth } from "../../contexts/AuthContext";
import api from "../../api/api";
import { useNavigate, useParams } from "react-router-dom";
import CloudUploadIcon from "@mui/icons-material/CloudUpload";
import styled from "styled-components";

export const VisuallyHiddenInput = styled("input")({
  clip: "rect(0 0 0 0)",
  clipPath: "inset(50%)",
  height: 1,
  overflow: "hidden",
  position: "absolute",
  bottom: 0,
  left: 0,
  whiteSpace: "nowrap",
  width: 1,
});

const UpdateRecipe = () => {
  const { recipeId } = useParams();
  const [recipeData, setRecipeData] = useState({
    title: "",
    preparationTime: "",
    instructions: [],
    isPremium: false,
    listOfIngredients: [],
    listOfNutritions: [],
  });

  const [openIngredientModal, setOpenIngredientModal] = useState(false);
  const [openNutritionModal, setOpenNutritionModal] = useState(false);
  const { user } = useAuth();
  const [image, setImage] = useState(null);

  const handleImageChange = (e) => {
    const file = e.target.files[0];
    setImage(file);
  };

  const [snackbar, setSnackbar] = useState({
    message: "",
    alertType: "",
  });
  const [openSnackbar, setOpenSnackbar] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchRecipeData = async () => {
      try {
        const response = await api.get(`/recipes/${recipeId}`);
        const {
          title,
          preparationTime,
          instructions,
          premium,
          ingredientsList,
          nutritionsList,
        } = response.data;

        setRecipeData({
          title,
          preparationTime,
          instructions,
          isPremium: premium === "Premium",
          listOfIngredients: ingredientsList,
          listOfNutritions: nutritionsList,
        });
      } catch (error) {
        console.error("Error fetching recipe:", error);
      }
    };

    fetchRecipeData();
  }, [recipeId]);

  const handleItemsSave = async (data, key) => {
    setRecipeData({ ...recipeData, [key]: data });
  };

  const handleInputChange = (field, value) => {
    setRecipeData((prevData) => {
      if (field === "isPremium") {
        return { ...prevData, [field]: !prevData.isPremium };
      } else if (field === "instructions") {
        const instructionsArray = value.split("\n");
        return { ...prevData, [field]: instructionsArray };
      }
      return { ...prevData, [field]: value };
    });
  };

  const handleUpdateRecipe = async () => {
    // Your logic to update the recipe data
    const {
      title,
      preparationTime,
      instructions,
      isPremium,
      listOfIngredients,
      listOfNutritions,
    } = recipeData;

    try {
      // Basic validation
      if (
        !title ||
        !preparationTime ||
        !instructions ||
        instructions.length === 0
      ) {
        setSnackbar({
          message: "All fields are required!",
          alertType: "error",
        });
        setOpenSnackbar(true);
        return;
      }

      if (
        listOfIngredients.length === 0 ||
        listOfIngredients.find(
          ({ itemName, quantity }) => !itemName && !quantity
        )
      ) {
        setSnackbar({
          message: "Please fill out ingredients fields.",
          alertType: "error",
        });
        setOpenSnackbar(true);
        return;
      }

      if (
        listOfNutritions.length === 0 ||
        listOfNutritions.find(
          ({ itemName, quantity }) => !itemName && !quantity
        )
      ) {
        setSnackbar({
          message: "Please fill out nutrition fields.",
          alertType: "error",
        });
        setOpenSnackbar(true);
        return;
      }
      const formData = new FormData();
      formData.append("image", image);
      formData.append(
        "recipeDto",
        JSON.stringify({
          title,
          preparationTime,
          instructions,
          premium: isPremium ? "Premium" : "Free",
          ingredientsList: listOfIngredients,
          nutritionsList: listOfNutritions,
          userId: user?.userId,
          userName: user?.firstName + " " + user?.lastName,
        })
      );

      await api.put(`/recipes/${recipeId}`, formData);

      setSnackbar({
        message: "Recipe updated successfully",
        alertType: "success",
      });
      setOpenSnackbar(true);
      setTimeout(() => {
        navigate("/user/my-recipes");
      }, 1000);
    } catch (err) {
      setSnackbar({ message: err?.response.data, alertType: "error" });
      setOpenSnackbar(true);
      console.error(err);
      return "fail";
    }
  };

  console.log("recipeData ", recipeData);

  return (
    <Box
      sx={{
        width: "50%",
        margin: "auto", // Center the box horizontally
        display: "flex",
        flexDirection: "column",
        alignItems: "center", // Center the content horizontally
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
      <Typography variant="h5" gutterBottom>
        Update Recipe
      </Typography>
      <TextField
        label="Title"
        variant="outlined"
        size="small"
        fullWidth
        margin="normal"
        value={recipeData.title}
        onChange={(e) => handleInputChange("title", e.target.value)}
      />
      <TextField
        label="Preparation Time"
        variant="outlined"
        fullWidth
        size="small"
        margin="normal"
        value={recipeData.preparationTime}
        onChange={(e) => handleInputChange("preparationTime", e.target.value)}
      />
      <TextField
        label="Instructions"
        variant="outlined"
        fullWidth
        multiline
        rows={3}
        margin="normal"
        value={recipeData.instructions.join("\n")}
        onChange={(e) => handleInputChange("instructions", e.target.value)}
      />
      <Box mt={2} sx={{ display: "flex", alignSelf: "flex-start" }}>
        <Button
          component="label"
          variant="contained"
          startIcon={<CloudUploadIcon />}
        >
          {image?.name
            ? image.name.length > 30
              ? `${image.name.slice(0, 30)}...`
              : image.name
            : "Upload recipe image"}

          <VisuallyHiddenInput
            type="file"
            accept="image/*"
            onChange={handleImageChange}
            sx={{ marginTop: 2 }}
          />
        </Button>
      </Box>

      <Box mt={2} sx={{ display: "flex", alignItems: "center" }}>
        <Typography variant="body1" style={{ marginRight: 8 }}>
          Premium Recipe
        </Typography>
        <Checkbox
          checked={recipeData.isPremium}
          onChange={() => handleInputChange("isPremium")}
        />
      </Box>

      <Box mt={2} sx={{ display: "flex", gap: 4 }}>
        <Button
          variant="outlined"
          color="primary"
          onClick={() => setOpenIngredientModal(true)}
        >
          Add Ingredient
        </Button>
        <Button
          variant="outlined"
          color="primary"
          onClick={() => setOpenNutritionModal(true)}
        >
          Add Nutrition
        </Button>
      </Box>
      <Box mt={2}>
        <Button
          fullWidth
          variant="contained"
          color="primary"
          onClick={handleUpdateRecipe}
        >
          Update Recipe
        </Button>
      </Box>

      {/* Ingredient Modal */}
      <ItemQuantityModal
        open={openIngredientModal}
        onClose={() => setOpenIngredientModal(false)}
        handleSaveItems={handleItemsSave}
        title="Add Ingredient"
        type="ingredient"
        itemsData={recipeData.listOfIngredients}
      />

      {/* Nutrition Modal */}
      <ItemQuantityModal
        open={openNutritionModal}
        onClose={() => setOpenNutritionModal(false)}
        handleSaveItems={handleItemsSave}
        title="Add Nutrition"
        type="nutrition"
        itemsData={recipeData.listOfNutritions}
      />
    </Box>
  );
};

export default UpdateRecipe;
