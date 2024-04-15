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
  Delete as DeleteIcon,
} from "@mui/icons-material";
import { useNavigate, useParams } from "react-router-dom";
import api from "../../api/api";
import CustomSnackbar from "../home/CustomSnackbar";

const AdminRecipeCommentsList = () => {
  const navigate = useNavigate();
  const { recipeId } = useParams();
  const [comments, setComments] = useState([]);
  const [snackbar, setSnackbar] = useState({
    message: "",
    alertType: "",
  });
  const [openSnackbar, setOpenSnackbar] = useState(false);

  const fetchComments = async () => {
    try {
      const response = await api.get(`/recipes/${recipeId}/comments`);
      setComments(response.data);
    } catch (error) {
      console.error("Error fetching comments:", error);
    }
  };

  useEffect(() => {
    fetchComments();
  }, [recipeId]);

  const handleCommentApproval = async (commentId, status) => {
    try {
      await api.put(`/comments/${commentId}/status-approval`, {
        status,
      });
      setSnackbar({
        message: "Comment " + status,
        alertType: "success",
      });
      setOpenSnackbar(true);
      fetchComments();
    } catch (err) {
      setSnackbar({ message: err?.response.data, alertType: "error" });
      setOpenSnackbar(true);
      console.error(err);
    }
  };

  const handleCommentDelete = async (commentId) => {
    try {
      await api.delete(`/comments/${commentId}`);
      setSnackbar({
        message: "Comment deleted",
        alertType: "success",
      });
      setOpenSnackbar(true);
      fetchComments();
    } catch (err) {
      setSnackbar({ message: err?.response.data, alertType: "error" });
      setOpenSnackbar(true);
      console.error(err);
    }
  };

  return (
    <Box>
      <Typography variant="h4" gutterBottom>
        Recipe Comments
      </Typography>

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
{comments&&comments.length?
      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>
                <b>Comment</b>
              </TableCell>
              <TableCell>
                <b>User</b>
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
            {comments.map((comment) => (
              <TableRow key={comment.commentId}>
                <TableCell>{comment.comment}</TableCell>
                <TableCell>{comment.userName}</TableCell>
                <TableCell>{comment.status}</TableCell>
                <TableCell>
                  <Button
                    variant="outlined"
                    color="primary"
                    startIcon={<ThumbUpIcon />}
                    onClick={() =>
                      handleCommentApproval(comment.commentId, "Approved")
                    }
                  >
                    Approve
                  </Button>
                  <Button
                    variant="outlined"
                    color="primary"
                    startIcon={<ThumbDownIcon />}
                    onClick={() =>
                      handleCommentApproval(comment.commentId, "Rejected")
                    }
                    sx={{ marginLeft: 2 }}
                  >
                    Reject
                  </Button>
                  <Button
                    variant="outlined"
                    color="secondary"
                    startIcon={<DeleteIcon />}
                    onClick={() => handleCommentDelete(comment.commentId)}
                    sx={{ marginLeft: 2 }}
                  >
                    Delete
                  </Button>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
: <Typography variant="h4" component="h3" color="red">No Comments yet this Moment</Typography>}
    </Box>
  );
};

export default AdminRecipeCommentsList;
