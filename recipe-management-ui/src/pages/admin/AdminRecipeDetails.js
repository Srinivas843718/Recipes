import React, { useState } from "react";
import {
  Box,
  Card,
  CardContent,
  Typography,
  Button,
  List,
  ListItem,
  ListItemText,
  Divider,
  Snackbar,
} from "@mui/material";
import { useNavigate } from "react-router-dom";

const AdminRecipeDetails = () => {
  const navigate = useNavigate();
  const [comments, setComments] = useState([]);
  const [snackbarOpen, setSnackbarOpen] = useState(false);

  const handleApproveComment = (commentId) => {
    // Logic to update the comment's approval status
    const updatedComments = comments.map((comment) =>
      comment.id === commentId ? { ...comment, approved: true } : comment
    );
    setComments(updatedComments);
    setSnackbarOpen(true);
  };

  return (
    <Box padding={4}>
      <Card>
        <CardContent>
          <Typography variant="h4" gutterBottom>
            Recipe Comments
          </Typography>
          <List>
            {comments.map((comment) => (
              <React.Fragment key={comment.id}>
                <ListItem>
                  <ListItemText primary={comment.text} />
                  {!comment.approved && (
                    <Button
                      variant="outlined"
                      color="primary"
                      onClick={() => handleApproveComment(comment.id)}
                    >
                      Approve
                    </Button>
                  )}
                </ListItem>
                <Divider />
              </React.Fragment>
            ))}
          </List>
        </CardContent>
        <Box display="flex" justifyContent="center" padding={2}>
          <Button
            variant="contained"
            color="primary"
            onClick={() => navigate("/admin/recipes")}
          >
            Back to Recipes
          </Button>
        </Box>
      </Card>
      <Snackbar
        open={snackbarOpen}
        autoHideDuration={3000}
        onClose={() => setSnackbarOpen(false)}
        message="Comment Approved"
      />
    </Box>
  );
};

export default AdminRecipeDetails;
