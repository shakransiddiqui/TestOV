import fs from "fs";
import path from "path";

const propertiesPath = path.resolve(__dirname, "..", "..", "..", "Configuration.properties");

let cachedProperties: Record<string, string> | null = null;

function parsePropertiesFile(): Record<string, string> {
  const fileContent = fs.readFileSync(propertiesPath, "utf8");
  const properties: Record<string, string> = {};

  for (const rawLine of fileContent.split(/\r?\n/)) {
    const line = rawLine.trim();
    if (!line || line.startsWith("#")) {
      continue;
    }

    const separatorIndex = line.indexOf("=");
    if (separatorIndex < 0) {
      continue;
    }

    const key = line.slice(0, separatorIndex).trim();
    const value = line.slice(separatorIndex + 1).trim();
    properties[key] = value;
  }

  return properties;
}

export function getProperty(key: string): string {
  if (!cachedProperties) {
    cachedProperties = parsePropertiesFile();
  }

  return cachedProperties[key] ?? key;
}

export function generateDynamicValue(propertyKey: string, rawValue: string): string {
  if (!rawValue.startsWith("DYNAMIC")) {
    return rawValue;
  }

  const timestamp = Date.now();

  if (propertyKey.toLowerCase().includes("email")) {
    return `playwright.${timestamp}@test.com`;
  }

  if (propertyKey.toLowerCase().includes("startup")) {
    return `PW Startup ${timestamp}`;
  }

  if (propertyKey.toLowerCase().includes("company") || propertyKey.toLowerCase().includes("organization")) {
    return `PW Organization ${timestamp}`;
  }

  return `${propertyKey}_${timestamp}`;
}

export function resolvePropertyValue(key: string): string {
  const propertyValue = getProperty(key);
  return generateDynamicValue(key, propertyValue);
}
