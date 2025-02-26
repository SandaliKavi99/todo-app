import React from "react";
import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import Home from "../pages/Home";
import { server } from "./server.ts";

describe("Task Application Integration Test", () => {
  test("user can add and mark tasks as done", async () => {
    render(<Home />);

    // Wait for tasks to load
    await screen.findByText("Task 1");

    // Add a new task
    fireEvent.change(screen.getByPlaceholderText("Title"), { target: { value: "New Task" } });
    fireEvent.change(screen.getByPlaceholderText("Description"), { target: { value: "New Desc" } });
    fireEvent.click(screen.getByText("Add"));

    // Mark task as done
    const doneButton = screen.getAllByText("Done")[0];
    fireEvent.click(doneButton);

    expect(screen.queryByText("Task 1")).not.toBeInTheDocument();
  });
});
