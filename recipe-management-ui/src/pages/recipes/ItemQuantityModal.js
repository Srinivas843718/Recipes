import AddCircleOutlineIcon from "@mui/icons-material/AddCircleOutline";
import RemoveCircleOutlineIcon from "@mui/icons-material/RemoveCircleOutline";
import {
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  IconButton,
  TextField,
  Select,
  MenuItem,
  InputLabel,
  FormControl,
} from "@mui/material";
import React, { useEffect, useState } from "react";

const labels = {
  ingredient: { title: "Ingredients" },
  nutrition: { title: "Nutrition" },
};

const VariantsModal = ({
  open,
  handleSaveItems,
  onClose,
  type = "ingredient",
  itemsData,
}) => {
  const [items, setItems] = useState([
    { itemName: "", quantity: "", measurement: "" },
  ]);
  const [itemErrors, setItemErrors] = useState([
    { itemName: false, quantity: false, measurement: false },
  ]);
  const [measurementTypes, setMeasurementTypes] = useState([
    "gram",
    "kg",
    "piece",
  ]);

  useEffect(() => {
    if (itemsData && itemsData.length > 0) {
      setItems(itemsData);
    } else setItems([{ itemName: "", quantity: "", measurement: "" }]);
  }, [itemsData]);

  const saveItems = () => {
    if (validateItems()) return;
    const key =
      type === "ingredient" ? "listOfIngredients" : "listOfNutritions";
    handleSaveItems(items, key);
    onModalClose();
  };

  const onModalClose = () => {
    onClose();
  };

  const handleAddItemRow = () => {
    setItems([...items, { itemName: "", quantity: "", measurement: "" }]);
    setItemErrors([
      ...itemErrors,
      { itemName: false, quantity: false, measurement: false },
    ]);
  };

  const handleRemoveItemRow = (index) => {
    if (items.length > 1) {
      const updatedItems = [...items];
      updatedItems.splice(index, 1);
      setItems(updatedItems);

      const updatedErrors = [...itemErrors];
      updatedErrors.splice(index, 1);
      setItemErrors(updatedErrors);
    }
  };

  const validateItems = () => {
    const updatedErrors = items.map(() => ({
      itemName: false,
      quantity: false,
      measurement: false,
    }));

    items.forEach((item, index) => {
      if (!item.itemName.trim()) {
        updatedErrors[index].itemName = true;
      }
      if (!item.quantity.trim()) {
        updatedErrors[index].quantity = true;
      }
      if (type === "ingredient" && !item.measurement) {
        updatedErrors[index].measurement = true;
      }
    });

    setItemErrors(updatedErrors);
    return updatedErrors.some((error) => Object.values(error).includes(true));
  };

  const handleItemChange = (index, field, value) => {
    const updatedItems = [...items];
    updatedItems[index][field] = value;
    setItems(updatedItems);
  };

  return (
    <Dialog
      fullWidth={type === "ingredient"}
      maxWidth="md"
      open={open}
      onClose={onModalClose}
    >
      <DialogTitle>Add {labels[type].title}</DialogTitle>
      <DialogContent>
        {items.map((item, index) => (
          <div
            key={index}
            style={{
              display: "flex",
              alignItems: "center",
              marginBottom: 16,
            }}
          >
            <TextField
              label="Item name"
              margin="normal"
              size="small"
              value={item.itemName}
              onChange={(e) =>
                handleItemChange(index, "itemName", e.target.value)
              }
              error={itemErrors[index]?.itemName}
              helperText={
                itemErrors[index]?.itemName ? "Item name is required" : ""
              }
              style={{ marginRight: 8 }}
            />
            <TextField
              label="Quantity"
              margin="normal"
              size="small"
              value={item.quantity}
              onChange={(e) =>
                handleItemChange(index, "quantity", e.target.value)
              }
              error={itemErrors[index]?.quantity}
              helperText={
                itemErrors[index]?.quantity ? "Quantity is required" : ""
              }
              style={{ marginRight: 8 }}
            />
            {type === "ingredient" && (
              <FormControl
                style={{ marginRight: 8 }}
                sx={{ width: "30%" }}
                size="small"
                InputLabelProps={{ shrink: true }}
              >
                <InputLabel>Measurement</InputLabel>
                <Select
                  InputLabelProps={{ shrink: true }}
                  value={item.measurement}
                  onChange={(e) =>
                    handleItemChange(index, "measurement", e.target.value)
                  }
                  error={itemErrors[index]?.measurement}
                >
                  {measurementTypes.map((type) => (
                    <MenuItem key={type} value={type}>
                      {type}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
            )}
            <IconButton
              color="primary"
              onClick={handleAddItemRow}
              style={{ marginRight: 8 }}
            >
              <AddCircleOutlineIcon />
            </IconButton>
            <IconButton
              color="secondary"
              onClick={() => handleRemoveItemRow(index)}
            >
              <RemoveCircleOutlineIcon />
            </IconButton>
          </div>
        ))}
      </DialogContent>
      <DialogActions>
        <Button onClick={onModalClose}>Cancel</Button>
        <Button variant="contained" color="primary" onClick={saveItems}>
          Save
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default VariantsModal;
