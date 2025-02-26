import { setupServer } from "msw/node";
import { rest } from 'msw';

export const server = setupServer(
  rest.get("/api/tasks", (req, res, ctx) => {
    return res(
      ctx.json([
        { id: 1, title: "Task 1", description: "Desc 1" },
        { id: 2, title: "Task 2", description: "Desc 2" },
      ])
    );
  }),
  rest.post("/api/tasks", (req, res, ctx) => {
    return res(ctx.status(201));
  }),
  rest.put("/api/tasks/:id", (req, res, ctx) => {
    return res(ctx.status(200));
  })
);

// Start the mock server before tests
beforeAll(() => server.listen());
afterEach(() => server.resetHandlers());
afterAll(() => server.close());
