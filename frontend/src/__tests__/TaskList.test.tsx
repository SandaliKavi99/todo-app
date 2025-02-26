import React from "react";
import { render, screen, waitFor } from "@testing-library/react";
import TaskList from "../components/TaskList";
import * as api from "../api/taskApi";

jest.mock("../api/taskApi");

describe("TaskList Component", () => {
  it("renders TaskList correctly after fetching tasks", async () => {
    (api.getTasks as jest.Mock).mockResolvedValue([
      { id: 1, title: "Test Task", description: "Test Description" }
    ]);

    render(<TaskList />);

    await waitFor(() => {
      expect(screen.getByText("Test Task")).toBeInTheDocument();
    });
  });
});
