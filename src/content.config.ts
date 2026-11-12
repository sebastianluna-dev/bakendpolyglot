import { defineCollection, z } from 'astro:content';
import { docsLoader } from '@astrojs/starlight/loaders';
import { docsSchema } from '@astrojs/starlight/schema';

export const collections = {
  docs: defineCollection({
    loader: docsLoader(),
    schema: docsSchema({
      extend: z.object({
        // Book metadata used by the print edition
        chapter: z.number().optional(),
        part: z.string().optional(),
        kicker: z.string().optional(), // one-line subtitle under the chapter title
      }),
    }),
  }),
};
