import React, { useState } from "react";
import { Button, Container, Snackbar, Typography, Box } from "@mui/material";
import api from "../../api/api";
import { useAuth } from "../../contexts/AuthContext";
import UserProfileForm from "./UserProfileForm";

const UserProfile = () => {
  const { user, updateUser, isUser } = useAuth();
  const [editingFields, setEditingFields] = useState({
    firstName: user.firstName,
    lastName: user.lastName,
    email: user.email,
    phone: user.phone || user.phoneNumber,
  });
  const [isEditing, setIsEditing] = useState(false);
  const [snackbarMessage, setSnackbarMessage] = useState("");
  const [snackbarOpen, setSnackbarOpen] = useState(false);

  const handleEdit = () => {
    setIsEditing(true);
  };

  const handleCancel = () => {
    setEditingFields({
      firstName: user.firstName,
      lastName: user.lastName,
      email: user.email,
      phone: user.phone,
    });
    setIsEditing(false);
  };

  const handleSave = async () => {
    const URL = `/users/${user?.userId}`;

    try {
      const res = await api.put(URL, editingFields);
      const data = res.data;
      updateUser({ ...user, ...data, phone: data.phone });
      setSnackbarMessage("Update successful.");
      setSnackbarOpen(true);
      setIsEditing(false);
    } catch (error) {
      setSnackbarMessage("Update failed.");
      setSnackbarOpen(true);
    }
  };

  const handleFieldChange = (field, value) => {
    let finalObj = { ...editingFields };
    finalObj = { ...finalObj, [field]: value };
    setEditingFields({ ...finalObj });
  };

  const handleSnackbarClose = () => {
    setSnackbarOpen(false);
  };

  return (
    <Container maxWidth="sm">
      <Box sx={{ padding: "20px" }}>
        <Typography variant="h4">User Profile</Typography>
        {isEditing ? (
          <UserProfileForm
            editingFields={editingFields}
            onFieldChange={handleFieldChange}
            onSave={handleSave}
            onCancel={handleCancel}
          />
        ) : (
          <>
            <Typography variant="subtitle1" sx={{ my: 2 }}>
              First Name: {user.firstName}
            </Typography>
            <Typography variant="subtitle1" sx={{ my: 2 }}>
              Last Name: {user.lastName}
            </Typography>
            <Typography variant="subtitle1" sx={{ my: 2 }}>
              Email: {user.email}
            </Typography>
            <Typography variant="subtitle1" sx={{ my: 2 }}>
              Phone: {editingFields.phone}
            </Typography>
            <Button variant="contained" onClick={handleEdit} sx={{ my: 2 }}>
              Edit
            </Button>
          </>
        )}
      </Box>
      <Snackbar
        open={snackbarOpen}
        autoHideDuration={3000}
        onClose={handleSnackbarClose}
        message={snackbarMessage}
      />
    </Container>
  );
};

export default UserProfile;
